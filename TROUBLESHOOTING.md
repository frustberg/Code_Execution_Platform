# 🚨 FAILED TO FETCH - TROUBLESHOOTING GUIDE

## ✅ ISSUE FIXED: Removed Redis dependency

The "Failed to fetch" error was likely caused by Redis rate limiter dependency. This version removes Redis to simplify the setup.

---

## 🔍 STEP-BY-STEP DIAGNOSIS

### **Step 1: Check if Services Are Running**

```bash
# Check Auth Service
curl http://localhost:8081/actuator/health

# Expected: {"status":"UP"}
# If error: Auth service not running

# Check Backend
curl http://localhost:8082/actuator/health

# Expected: {"status":"UP"}
# If error: Backend not running

# Check API Gateway
curl http://localhost:8080/actuator/health

# Expected: {"status":"UP"}
# If error: Gateway not running
```

### **Step 2: Test API Gateway Routing**

```bash
# Test auth route through gateway
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Expected: {"token":"eyJ...","userId":"..."}
# If error: Gateway not routing properly
```

### **Step 3: Check Browser Console**

1. Open browser DevTools (F12)
2. Go to Console tab
3. Try to register/login
4. Look for error messages

**Common errors:**
- `net::ERR_CONNECTION_REFUSED` → Service not running
- `CORS error` → CORS misconfigured
- `404 Not Found` → Wrong URL
- `Failed to fetch` → Service not reachable

### **Step 4: Check Network Tab**

1. Open DevTools (F12)
2. Go to Network tab
3. Try to register/login
4. Look at failed request

**What to check:**
- Request URL: Should be `http://localhost:8080/api/auth/...`
- Status: Should be 200 (if 0, service down)
- Response: Check error message

---

## 🛠️ SOLUTIONS

### **Solution 1: Restart All Services**

```bash
# Stop everything
docker-compose down
pkill -f "spring-boot:run"
pkill -f "npm run dev"

# Clear old builds
cd auth-service && mvn clean && cd ..
cd backend && mvn clean && cd ..
cd api-gateway && mvn clean && cd ..

# Start fresh
./start-all.sh
```

### **Solution 2: Check Ports Are Free**

```bash
# Check if ports are in use
netstat -an | grep "8080"  # API Gateway
netstat -an | grep "8081"  # Auth Service
netstat -an | grep "8082"  # Backend
netstat -an | grep "5173"  # Frontend

# If ports are in use, kill processes
# Windows:
netstat -ano | findstr ":8080"
taskkill /PID <PID> /F

# Linux/Mac:
lsof -ti:8080 | xargs kill -9
```

### **Solution 3: Wait for Services to Start**

Services take time to start. Wait for:
```
✓ MongoDB is ready
✓ Auth Service started     ← WAIT FOR THIS
✓ Backend started          ← WAIT FOR THIS
✓ API Gateway started      ← WAIT FOR THIS
✓ Frontend started
```

**Don't try to login/register before all services show ✓**

### **Solution 4: Check Logs**

```bash
# Auth Service logs
tail -f logs/auth-service.log

# Backend logs
tail -f logs/backend.log

# API Gateway logs
tail -f logs/api-gateway.log

# Look for errors like:
# - "Address already in use"
# - "Connection refused"
# - "Cannot connect to MongoDB"
```

### **Solution 5: Test Each Service Individually**

```bash
# Test Auth Service directly (bypass gateway)
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# If this works but gateway doesn't, gateway is the issue
# If this fails, auth service is the issue
```

---

## 🎯 QUICK FIXES

### **Fix 1: Clear Browser Cache**
```
Ctrl + Shift + Delete
→ Clear cache and cookies
→ Reload page
```

### **Fix 2: Check MongoDB**
```bash
docker ps
# Should see: mongodb-codeplatform

docker exec -it mongodb-codeplatform mongosh
use codeplatform
db.users.count()
# Should be > 0
```

### **Fix 3: Verify Frontend Config**
```bash
cd frontend
cat src/api/client.ts

# Should have:
# baseURL: 'http://localhost:8080/api'
```

### **Fix 4: Check CORS**
Open browser console and look for:
```
Access to fetch at 'http://localhost:8080/api/auth/login' 
from origin 'http://localhost:5173' has been blocked by CORS
```

If you see this, CORS is misconfigured (but shouldn't be in this version).

---

## 📋 COMPLETE VERIFICATION CHECKLIST

Run these commands in order:

```bash
# 1. Check Docker
docker --version
docker ps | grep mongodb

# 2. Check MongoDB
docker exec -it mongodb-codeplatform mongosh --eval "db.adminCommand('ping')"

# 3. Check Auth Service
curl -s http://localhost:8081/actuator/health | grep UP

# 4. Check Backend
curl -s http://localhost:8082/actuator/health | grep UP

# 5. Check API Gateway
curl -s http://localhost:8080/actuator/health | grep UP

# 6. Test Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# All should return SUCCESS ✅
```

---

## 🚀 START FROM SCRATCH

If nothing works:

```bash
# 1. Stop everything
docker-compose down
docker rm -f mongodb-codeplatform 2>/dev/null || true
pkill -f java
pkill -f node

# 2. Clean all builds
find . -name "target" -type d -exec rm -rf {} + 2>/dev/null
find . -name "node_modules" -type d -exec rm -rf {} + 2>/dev/null

# 3. Start MongoDB only
docker-compose up -d
sleep 10

# 4. Build auth service
cd auth-service
mvn clean install -DskipTests
mvn spring-boot:run &
cd ..
sleep 30

# 5. Test auth service
curl http://localhost:8081/actuator/health

# 6. If step 5 works, continue with start-all.sh
# If step 5 fails, check auth-service logs
```

---

## 💡 COMMON CAUSES

| Error | Cause | Solution |
|-------|-------|----------|
| Failed to fetch | Service not running | Run `./start-all.sh` |
| Connection refused | Wrong port | Check `http://localhost:8080` |
| CORS error | CORS misconfigured | Already fixed in this version |
| 404 Not Found | Wrong endpoint | Check URL starts with `/api/auth/` |
| Timeout | Service slow to start | Wait 2-3 minutes |

---

## 🧪 WORKING EXAMPLE

**What should happen:**

```bash
# 1. Start services
./start-all.sh
# Wait for all ✓ marks

# 2. Open browser
http://localhost:5173

# 3. Click Register
# Fill form
# Click Submit
# → Should see: "Registration successful"

# 4. Go to Login
# Enter credentials
# → Should redirect to Problems page

# 5. Browser console (F12)
localStorage.getItem('authToken')
# → Should show: "eyJhbGc..."
```

---

## 📞 STILL NOT WORKING?

1. **Check service order:**
   - MongoDB must start first
   - Auth must start before Gateway
   - All must be UP before using UI

2. **Check logs/**
   ```bash
   ls logs/
   cat logs/api-gateway.log | tail -50
   cat logs/auth-service.log | tail -50
   ```

3. **Verify with curl:**
   ```bash
   # This MUST work:
   curl http://localhost:8081/actuator/health
   
   # Then this:
   curl http://localhost:8080/actuator/health
   ```

4. **If curl works but browser doesn't:**
   - Clear browser cache
   - Try incognito mode
   - Check browser console for errors

---

**🎊 Most issues are solved by waiting for services to fully start!**
