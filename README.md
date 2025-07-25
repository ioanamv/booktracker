# Book Tracker Mobile App

A mobile app to manage personal reading list. Users can log in, add books with title, author, reading status (**To Read**, **In Progress**, **Completed**), completion date, and a 1–5 star rating.

Authentication is handled via **Firebase**, with a **Flutter frontend** and a **Spring Boot backend** using **PostgreSQL**.

## Backend
This backend provides a secure REST API for managing user-specific book data. It handles authentication via Firebase, stores data in PostgreSQL, and exposes endpoints for all CRUD operations and filtering by reading status.
