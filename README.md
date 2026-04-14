# 📦 Simply Stock – Mobile Inventory Management App

Simply Stock is a role-based mobile inventory management application designed for small businesses. The goal of this project was to create a simple, scalable, and free solution that allows businesses to track inventory in real time while maintaining employee accountability.

---

## 🔐 Role-Based System

This application is built around two roles:

### 👨‍💼 Manager
Managers have full control over the inventory system:
- Create new items with descriptions, categories, and optional URLs  
- Upload images directly from the mobile device gallery when creating items  
- Add quantity to existing inventory (check-in system)  
- View consumption analytics based on employee activity  
- Delete existing user profiles  
- Navigate through managerial tools using a clean action bar
- Swap roles

When creating items, the app accesses the device’s gallery using Android’s built-in content picker. The selected image is stored as a URI and used throughout the app to visually represent each item.

---

### 👷 Employee
Employees interact with a live version of the inventory:
- View real-time inventory updates  
- Add items to a cart (only up to available quantity)  
- Adjust item quantities directly in the cart  
- Remove items if needed  
- Finalize checkout, which updates inventory and logs the transaction  

The cart acts as a temporary holding system. Items are not permanently changed until checkout is confirmed, ensuring accuracy and flexibility.

---

## 🗄️ Database Design (SQLite)

SQLite is used as the backbone of the system to manage all data:

- **Users Table** → stores user accounts  
- **Items Table** → stores inventory items  
- **Checkouts Table** → logs all transactions  
- **Organizations Table** → supports multiple businesses  

This structure allows:
- tracking who checked out what  
- when items were checked out  
- how much inventory remains  

---

## 📍 Multi-Location Support

The system was originally designed for a single location, but was improved to support multiple businesses (organizations). Each business has its own:
- inventory  
- employees  
- checkout records  

This makes the system scalable and usable across different environments.

---

## 🔒 Additional Features

- User authentication system  
- Email verification  
- Password requirements for security  
- Logout functionality with activity reset  
- Manager-only access to sensitive features  
- Clean navigation using action bars  

---

## 📊 Consumption Analytics

Managers can:
- View users who have checked out items  
- Select specific dates  
- See a visual breakdown of item usage  

This feature uses a bar chart to display:
- item names  
- quantity checked out  

---

## 📈 AnyChart Integration

This was my first time using a third-party library from GitHub.

I used **AnyChart** to create the analytics graphs:
- Easy to integrate using documentation and YouTube tutorials  
- Pre-built chart structures made implementation straightforward  
- Built-in animations improved the overall user experience  

This was a great introduction to working with external libraries and understanding how to integrate them into an Android project.

---

## 🚀 Future Improvements

Planned features include:
- Camera integration for taking item photos directly  
- Low stock alerts  
- Direct URL links for restocking items  
- Improved UI/UX design and animations  
- More advanced analytics with filtering (daily, weekly, monthly) to track item consumption per employee  

---

## 🌱 Vision

The long-term goal is to keep this system:
- **simple**
- **scalable**
- **free for small businesses**

I plan to:
- create a Google Developer account  
- prepare the app for release  
- publish it on the Google Play Store  

---

## 🛠️ Tech Stack

- Java  
- Android Studio  
- SQLite  
- XML  

---

## 💭 Development Approach (FDD)

This project was developed using a **Feature-Driven Development (FDD)** approach.

- Focused on building small, manageable features  
- Followed a Gantt chart for planning  
- Adjusted timeline as needed  
- Allowed flexibility without strict time constraints  

Even though I have struggled with time management in the past, FDD helped me:
- stay organized  
- reduce stress  
- steadily build progress  

---

## 🏁 Final Thoughts

This project represents my first full entry-level full stack mobile application.

From database design to UI, to implementing real-world features like carts, checkouts, and analytics — this was a major step forward in my development journey.

I’m proud of how far I’ve come with:
- problem solving  
- debugging  
- building a complete system from scratch  

And this is only the beginning.
