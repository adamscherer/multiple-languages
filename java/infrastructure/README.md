# AWS Deployment Instructions

This guide explains how to deploy the Bookstore API to AWS using CloudFormation and GitHub Actions.

## Prerequisites

1. AWS Account with appropriate permissions
2. GitHub repository with the code
3. AWS CLI installed and configured
4. GitHub Actions enabled in your repository

## Required GitHub Secrets

Add the following secrets to your GitHub repository:

1. `AWS_ROLE_ARN`: ARN of the IAM role for GitHub Actions (created by github-role.yml)
2. `JWT_SECRET`: Secret key for JWT token generation

## IAM Role Setup

1. Deploy the GitHub Actions IAM role:
   ```bash
   aws cloudformation deploy \
     --template-file infrastructure/github-role.yml \
     --stack-name github-actions-role \
     --capabilities CAPABILITY_NAMED_IAM \
     --parameter-overrides \
       GitHubOrg=your-github-org \
       RepositoryName=multiple-languages
   ```

2. Get the role ARN and add it to GitHub Secrets:
   ```bash
   aws cloudformation describe-stacks \
     --stack-name github-actions-role \
     --query 'Stacks[0].Outputs[?ExportName==`github-actions-multiple-languages-role-arn`].OutputValue' \
     --output text
   ```

   Add the output as `AWS_ROLE_ARN` in your GitHub repository secrets.

## Infrastructure Components

The deployment creates the following AWS resources:

1. VPC with:
   - 2 public subnets across different AZs
   - Internet Gateway
   - Route tables

2. ECS Cluster with:
   - Fargate tasks
   - Application Load Balancer
   - Target Group
   - Security Groups
   - CloudWatch Log Group

## Deployment Process

1. Push your code to the main branch or manually trigger the workflow:
   ```bash
   git push origin main
   ```

2. The GitHub Actions workflow will:
   - Build the Java application
   - Create a Docker image
   - Push the image to Amazon ECR
   - Deploy/update the VPC stack
   - Deploy/update the ECS stack
   - Force a new deployment of the ECS service

3. Monitor the deployment:
   - Check the GitHub Actions workflow status
   - Monitor the CloudFormation stacks in AWS Console
   - Check the ECS service status

## Accessing the API

After deployment, you can access the API at:
```
http://<load-balancer-dns>/api
```

The Load Balancer DNS will be available in the CloudFormation outputs.

## Environment Variables

The following environment variables are configured in the ECS task definition:

- `SPRING_PROFILES_ACTIVE`: Set to the deployment environment (dev/staging/prod)
- `JWT_SECRET`: Secret key for JWT token generation

## Scaling

To modify the number of running tasks:

1. Update the `DesiredCount` parameter in `ecs.yml`
2. Redeploy the ECS stack

## Monitoring

- Application logs are available in CloudWatch Logs
- Service metrics are available in CloudWatch Metrics
- Container insights are enabled for the ECS cluster

## Troubleshooting

1. Check CloudWatch Logs for application errors
2. Verify security group rules if experiencing connectivity issues
3. Check target group health checks if the service is unhealthy
4. Verify task definition if containers fail to start
