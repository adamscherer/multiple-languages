## 1. Overview

The API will support a complete ecommerce flow including user authentication, browsing and managing a book catalog, shopping cart management, order placement, and payment processing. Special attention will be given to idempotency for order placement to avoid duplicate orders.

---

## 2. Functional Requirements

### 2.1 Authentication & User Management

- **User Registration**
  - **Endpoint:** `POST /api/auth/register`
  - **Inputs:** `name`, `email`, `password`
  - **Output:** User details along with a JWT token

- **User Login**
  - **Endpoint:** `POST /api/auth/login`
  - **Inputs:** `email`, `password`
  - **Output:** User details along with a JWT token

- **User Profile Retrieval**
  - **Endpoint:** `GET /api/users/me`
  - **Requirement:** Must include a valid authentication token in the header
  - **Output:** User profile information

### 2.2 Book Catalog

- **List Books**
  - **Endpoint:** `GET /api/books`
  - **Features:**
    - Support query parameters for filtering (search term, category, author)
    - Support sorting (e.g., by price or title) and pagination (using `page` and `limit`)
  - **Output:** A paginated list of books

- **Retrieve Book Details**
  - **Endpoint:** `GET /api/books/{id}`
  - **Input:** Book's unique identifier
  - **Output:** Detailed information about the book (including title, author, category, price, description, cover image URL, published date, and ISBN)

- **Add a New Book (Admin-only)**
  - **Endpoint:** `POST /api/books`
  - **Input:** Book details (title, author, category, price, description, cover image URL, published date, ISBN)
  - **Output:** Newly created book record
  - **Authorization:** Must be restricted to admin users via role-based access control

### 2.3 Shopping Cart

- **Retrieve Cart**
  - **Endpoint:** `GET /api/cart`
  - **Requirement:** Must include a valid authentication token
  - **Output:** The current user's shopping cart including items, quantities, and subtotal

- **Add Book to Cart**
  - **Endpoint:** `POST /api/cart`
  - **Input:** `bookId` and `quantity`
  - **Requirement:** Must include a valid authentication token
  - **Output:** Updated cart with confirmation message

- **Update Cart**
  - **Endpoint:** `PUT /api/cart`
  - **Input:** `bookId` and updated `quantity`
  - **Requirement:** Must include a valid authentication token
  - **Output:** Updated cart with confirmation message

- **Remove Book from Cart**
  - **Endpoint:** `DELETE /api/cart/{bookId}`
  - **Input:** Book’s unique identifier
  - **Requirement:** Must include a valid authentication token
  - **Output:** Updated cart after removal with confirmation message

### 2.4 Orders & Checkout

- **Place an Order**
  - **Endpoint:** `POST /api/orders`
  - **Inputs:**
    - Shipping address details (line1, line2, city, state, postal code, country)
    - Payment method details (card number, expiry month/year, CVV)
  - **Requirement:**
    - Must include a valid authentication token
    - **Idempotency:** Clients must include an `Idempotency-Key` header with each request. The server will check for existing orders with that key and return the existing order if found.
  - **Output:** Order details (orderId, items, subtotal, shipping cost, total, status, creation timestamp)

- **List Orders**
  - **Endpoint:** `GET /api/orders`
  - **Requirement:** Must include a valid authentication token
  - **Output:** A list of orders for the authenticated user

- **Retrieve Order Details**
  - **Endpoint:** `GET /api/orders/{orderId}`
  - **Input:** Order’s unique identifier
  - **Requirement:** Must include a valid authentication token
  - **Output:** Detailed order information (including shipping address, order items, totals, status, timestamps)

### 2.5 Payment (Optional Separate Endpoint)

- **Process Payment**
  - **Endpoint:** `POST /api/payment`
  - **Input:** Order identifier and payment method details
  - **Requirement:** Must include a valid authentication token
  - **Output:** Payment status and transaction identifier

### 2.6 Error Handling

- All endpoints must return standardized error responses. Example error format:

  ```json
  {
    "error": {
      "code": "INVALID_REQUEST",
      "message": "The provided email is not valid."
    }
  }
