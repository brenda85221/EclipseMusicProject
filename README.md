# Shopping Module – Deployment Version

This repository contains a **deployment-adapted backend** based on a larger team-developed music platform system.

The original project was developed collaboratively.  
For demonstration purposes, I modified parts of the backend configuration and deployment settings so the system can run on cloud platforms.

# System Architecture
```
User Frontend (Vue)
Admin Frontend (Vue)
        │
        │ REST API
        ▼
Backend API (Spring Boot)
        │
        ├ PostgreSQL (Supabase)
        └ Image Storage (Supabase Storage)
```


# Tech Stack
- **Frontend**: Vue.js  
- **Backend**: Java, Spring Boot, REST API, JWT Authentication  
- **Database & Storage**: PostgreSQL (Supabase), Supabase Storage  
- **Deployment**: Render (Backend), Netlify (Frontend)

# GitHub Repositories
- Frontend – User (Team Project – Private Repository)
  * https://github.com/brenda85221/team5-vue2025.git
- Frontend – Admin (Team Project – Private Repository)
  * https://github.com/brenda85221/musicplat.git
- Backend – Deployment Version (Team Project – modified by me for cloud deployment)
  * This repo
    
# Demo 

https://chenyu-musicweb.netlify.app

<img width="100" height="100" alt="music網頁前台" src="https://github.com/user-attachments/assets/318c1450-759b-49d7-b37e-b40579e7048a" />

- Optimized for **desktop browsers** (RWD not fully implemented)
- Showcases **shopping module only**
- Backend may take time to initialize due to cold start (Render)
  
- Test Account
  - account: testaccount 
  - Password: 123456
    
# My Contribution
- Developed both frontend and backend features for the **shopping module**
- Modified Spring Boot backend configuration to support cloud deployment  
- Integrated Supabase PostgreSQL and Storage  
- Deployed backend service on Render  



