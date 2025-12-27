#!/bin/bash

set -e

echo "🚀 Starting Code Execution Platform"
echo "===================================="
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Check Docker
echo "🐳 Checking Docker..."
if ! docker --version > /dev/null 2>&1; then
    echo -e "${RED}✗ Docker not found. Please install Docker first.${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Docker is installed${NC}"
echo ""

# Start MongoDB
echo "📊 Starting MongoDB..."
docker-compose up -d

# Wait for MongoDB
echo "Waiting for MongoDB to be ready..."
for i in {1..30}; do
    if docker exec mongodb-codeplatform mongosh --eval "db.adminCommand('ping')" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ MongoDB is ready${NC}"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}✗ MongoDB failed to start${NC}"
        exit 1
    fi
    sleep 2
done
echo ""

# Create logs directory
mkdir -p logs

# Start Auth Service
echo "🔐 Starting Auth Service (Port 8081)..."
cd auth-service
if [ ! -d "target" ]; then
    echo "Building auth-service..."
    mvn clean install -DskipTests > ../logs/auth-build.log 2>&1
fi
mvn spring-boot:run > ../logs/auth-service.log 2>&1 &
AUTH_PID=$!
cd ..

# Wait for Auth Service
echo "Waiting for Auth Service..."
for i in {1..60}; do
    if curl -s http://localhost:8081/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Auth Service started${NC}"
        break
    fi
    if [ $i -eq 60 ]; then
        echo -e "${RED}✗ Auth Service failed. Check logs/auth-service.log${NC}"
    fi
    sleep 2
done
echo ""

# Start Backend
echo "🔧 Starting Backend (Port 8082)..."
cd backend
if [ ! -d "target" ]; then
    echo "Building backend..."
    mvn clean install -DskipTests > ../logs/backend-build.log 2>&1
fi
mvn spring-boot:run > ../logs/backend.log 2>&1 &
BACKEND_PID=$!
cd ..

# Wait for Backend
echo "Waiting for Backend..."
for i in {1..60}; do
    if curl -s http://localhost:8082/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Backend started${NC}"
        break
    fi
    if [ $i -eq 60 ]; then
        echo -e "${RED}✗ Backend failed. Check logs/backend.log${NC}"
    fi
    sleep 2
done
echo ""

# Start API Gateway
echo "🌐 Starting API Gateway (Port 8080)..."
cd api-gateway
if [ ! -d "target" ]; then
    echo "Building api-gateway..."
    mvn clean install -DskipTests > ../logs/gateway-build.log 2>&1
fi
mvn spring-boot:run > ../logs/api-gateway.log 2>&1 &
GATEWAY_PID=$!
cd ..

# Wait for Gateway
echo "Waiting for API Gateway..."
for i in {1..60}; do
    if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✓ API Gateway started${NC}"
        break
    fi
    if [ $i -eq 60 ]; then
        echo -e "${RED}✗ API Gateway failed. Check logs/api-gateway.log${NC}"
    fi
    sleep 2
done
echo ""

# Start Frontend
echo "🎨 Starting Frontend (Port 5173)..."
cd frontend
if [ ! -d "node_modules" ]; then
    echo "Installing frontend dependencies..."
    npm install > ../logs/npm-install.log 2>&1
fi
npm run dev > ../logs/frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..

sleep 3
echo ""

# Cleanup function
cleanup() {
    echo ""
    echo "🛑 Shutting down services..."
    kill $AUTH_PID $BACKEND_PID $GATEWAY_PID $FRONTEND_PID 2>/dev/null || true
    docker-compose down
    echo "👋 Services stopped"
    exit 0
}

trap cleanup INT TERM

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "${GREEN}🎉 Code Execution Platform is running!${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📱 Access the application:"
echo "   http://localhost:5173"
echo ""
echo "🗄️ Services:"
echo "   API Gateway:  http://localhost:8080"
echo "   Auth Service: http://localhost:8081"
echo "   Backend:      http://localhost:8082"
echo "   MongoDB:      localhost:27017"
echo ""
echo "🔐 Default Admin:"
echo "   Username: admin"
echo "   Password: admin123"
echo ""
echo "📋 Features:"
echo "   ✅ JWT Authentication"
echo "   ✅ API Gateway with routing"
echo "   ✅ 5 DSA Problems"
echo "   ✅ Timeout (5-7 seconds)"
echo ""
echo "🧪 Test Endpoints:"
echo "   curl http://localhost:8081/actuator/health"
echo "   curl http://localhost:8082/actuator/health"
echo "   curl http://localhost:8080/actuator/health"
echo ""
echo "📊 Logs: ./logs/"
echo -e "${YELLOW}Press Ctrl+C to stop all services${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

wait
