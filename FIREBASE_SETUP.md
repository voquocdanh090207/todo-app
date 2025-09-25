# Firebase Configuration Guide

## Step 1: Create Firebase Project
1. Go to https://console.firebase.google.com/
2. Create a new project
3. Enable Firestore Database
4. Go to Project Settings > Service accounts
5. Generate new private key (JSON file)

## Step 2: Setup Google Cloud Credentials
1. Download the service account key JSON file
2. Place it in your project root or a secure location
3. Set environment variable:
   ```
   set GOOGLE_APPLICATION_CREDENTIALS=path\to\your\service-account-key.json
   ```

## Step 3: Update Firebase Configuration
1. Open `src/main/java/org/example/config/FirebaseInitializer.java`
2. Replace "your-project-id" with your actual Firebase project ID

## Step 4: Alternative Configuration (if using service account file directly)
If you want to use the service account file directly instead of environment variable:

```java
// In FirebaseInitializer.java, replace the credentials line with:
FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");

FirebaseOptions options = FirebaseOptions.builder()
    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    .setProjectId("your-project-id")
    .build();
```

## Firestore Database Rules
Set these rules in Firebase Console > Firestore Database > Rules:
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /todos/{document=**} {
      allow read, write: if true; // For development only - adjust for production
    }
  }
}
```

## Security Note
For production, implement proper authentication and authorization rules.
