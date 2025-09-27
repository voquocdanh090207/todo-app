# Todo List Application

## Mô tả
Ứng dụng Todo List đơn giản sử dụng:
- **Backend**: Java Servlet với SDK 17
- **Frontend**: AngularJS 1.x
- **Database**: Google Cloud Firestore
- **Build Tool**: Maven

## Tính năng
- ✅ Thêm todo mới
- ✅ Xem danh sách todos
- ✅ Chỉnh sửa todo
- ✅ Đánh dấu hoàn thành/chưa hoàn thành
- ✅ Xóa todo
- ✅ Lọc theo trạng thái (All, Pending, Completed)
- ✅ Lưu trữ trên Google Cloud Firestore
- ✅ Giao diện responsive với Bootstrap

## Cài đặt và Chạy

### 1. Cài đặt Dependencies
```cmd
mvn clean install
```

### 2. Cấu hình Firebase
1. Tạo project Firebase tại https://console.firebase.google.com/
2. Tạo Firestore database
3. Tải service account key JSON
4. Đặt biến môi trường:
```cmd
set GOOGLE_APPLICATION_CREDENTIALS=path\to\your\service-account-key.json
```
5. Cập nhật project ID trong `FirebaseInitializer.java`

### 3. Build và Deploy
```cmd
mvn clean package
```

### 4. Deploy lên Server
- Copy file `target/java-1.0-SNAPSHOT.war` vào thư mục webapps của Tomcat
- Hoặc chạy với Jetty/Tomcat embedded

### 5. Truy cập ứng dụng
Mở trình duyệt và truy cập: `http://localhost:8080/java-1.0-SNAPSHOT/`

## API Endpoints

### GET /api/todos
Lấy tất cả todos

### GET /api/todos/{id}
Lấy todo theo ID

### POST /api/todos
Tạo todo mới
```json
{
  "title": "Todo title",
  "description": "Todo description"
}
```

### PUT /api/todos/{id}
Cập nhật todo
```json
{
  "title": "Updated title",
  "description": "Updated description",
  "completed": true
}
```

### PUT /api/todos/{id}
Toggle trạng thái complete
```json
{
  "toggle": true
}
```

### DELETE /api/todos/{id}
Xóa todo

## Cấu trúc Project
```
src/
├── main/
│   ├── java/
│   │   └── org/example/
│   │       ├── config/
│   │       │   └── FirebaseInitializer.java
│   │       ├── model/
│   │       │   └── Todo.java
│   │       ├── service/
│   │       │   └── TodoService.java
│   │       └── servlet/
│   │           └── TodoServlet.java
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml
│       ├── css/
│       │   └── style.css
│       ├── js/
│       │   ├── app.js
│       │   ├── controllers/
│       │   │   └── todoController.js
│       │   └── services/
│       │       └── todoService.js
│       └── index.html
```

## Troubleshooting

### 1. Firebase Connection Issues
- Kiểm tra GOOGLE_APPLICATION_CREDENTIALS
- Đảm bảo service account có quyền Firestore
- Kiểm tra project ID trong FirebaseInitializer.java

### 2. CORS Issues
- Đã cấu hình CORS filter trong web.xml
- Kiểm tra endpoint URLs trong todoService.js

### 3. Build Issues
- Đảm bảo Java 17 đã được cài đặt
- Chạy `mvn clean install` để tải dependencies

## Technologies Used
- Java 17
- Maven
- Java Servlets
- Google Cloud Firestore
- AngularJS 1.8.2
- Bootstrap 5
- Font Awesome 6


