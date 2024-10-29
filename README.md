# GitHubViewer
### An application to view basic information about your GitHub repositories.

## Stack
+ Kotlin
+ MVVM
+ Hilt
+ Retrofit
+ Coroutines
+ StateFlow
+ Navigation Component
+ Fragment
+ SharedPreferences

## App video overview

https://youtu.be/VhKzFtlDmgk

## App info

The app support both portraint and landscape orientations.

At the moment, the app consists of three screens:

![three main screens](https://github.com/user-attachments/assets/99b5873b-0491-425c-aa0a-460aca8736f1)

Authorisation is done via a GitHub personal access tokens.

After authorisation, the token is stored. And each subsequent access to the application automatically confirms the validity of the token. If the token is valid, the user immediately gets to the screen of the list of his repositories.

The following screen states have been added to the screens:
- loading.
- no connection to the Internet.
- different types of errors with the description of the error.
- invalid input (for auth screen).
- empty list of repositories (for repositories screen).
  
In Repository details sreen states management goes separatly for general repository query and for readme file query.

![states management](https://github.com/user-attachments/assets/c904fa57-5db6-4585-83c9-465de92cdd5f)


