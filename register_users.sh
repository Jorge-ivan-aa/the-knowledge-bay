#!/bin/zsh

# Placeholder tokens - replace with actual tokens obtained after login
TOKEN_USER1="7bb5fe3d-d9d8-49b1-aa02-f3b1a0c53432"
TOKEN_USER2="9d1aa334-cb72-4156-aad4-4fc52eaaebf0"
TOKEN_USER3="4b9d594e-f089-483c-8f84-6e0a1821b997"
TOKEN_USER4="51e2d1c8-0bde-4193-b200-23d06d2f470a"
TOKEN_USER5="ff7d9d71-87ba-4770-81ce-9a4c68e3c7e0"
TOKEN_USER6="9a02480a-5e1d-4679-9c00-97db1432e1e3"

echo "Registering users..."
curl -X POST -H "Content-Type: application/json" -d '{"username": "user1_3interests", "email": "user1@example.com", "password": "password123", "dateOfBirth": "2000-01-01", "firstName": "Alice", "lastName": "Smith", "biography": "Loves all three interests.", "interests": ["InterestA", "InterestB", "InterestC"]}' http://localhost:8080/api/auth/register
echo ""

curl -X POST -H "Content-Type: application/json" -d '{"username": "user2_3interests", "email": "user2@example.com", "password": "password123", "dateOfBirth": "2000-02-02", "firstName": "Bob", "lastName": "Johnson", "biography": "Passionate about A, B, and C.", "interests": ["InterestA", "InterestB", "InterestC"]}' http://localhost:8080/api/auth/register
echo ""

curl -X POST -H "Content-Type: application/json" -d '{"username": "user3_3interests", "email": "user3@example.com", "password": "password123", "dateOfBirth": "2000-03-03", "firstName": "Charlie", "lastName": "Brown", "biography": "Enjoys InterestA, InterestB, and InterestC equally.", "interests": ["InterestA", "InterestB", "InterestC"]}' http://localhost:8080/api/auth/register
echo ""

curl -X POST -H "Content-Type: application/json" -d '{"username": "user4_2interests", "email": "user4@example.com", "password": "password123", "dateOfBirth": "2000-04-04", "firstName": "Diana", "lastName": "Davis", "biography": "Focused on InterestA and InterestB.", "interests": ["InterestA", "InterestB"]}' http://localhost:8080/api/auth/register
echo ""

curl -X POST -H "Content-Type: application/json" -d '{"username": "user5_2interests", "email": "user5@example.com", "password": "password123", "dateOfBirth": "2000-05-05", "firstName": "Eve", "lastName": "Wilson", "biography": "Likes InterestB and InterestC.", "interests": ["InterestB", "InterestC"]}' http://localhost:8080/api/auth/register
echo ""

curl -X POST -H "Content-Type: application/json" -d '{"username": "user6_1interest", "email": "user6@example.com", "password": "password123", "dateOfBirth": "2000-06-06", "firstName": "Frank", "lastName": "Miller", "biography": "Specializes in InterestA.", "interests": ["InterestA"]}' http://localhost:8080/api/auth/register
echo ""


curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user1@example.com",
    "password": "password123"
  }'

curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user2@example.com",
    "password": "password123"
  }'

  curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user3@example.com",
    "password": "password123"
  }'

  curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user4@example.com",
    "password": "password123"
  }'

  curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user5@example.com",
    "password": "password123"
  }'

curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user6@example.com",
    "password": "password123"
  }'



echo "Establishing follow relationships..."
# User1 follows User2 and User3
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer 7bb5fe3d-d9d8-49b1-aa02-f3b1a0c53432" http://localhost:8080/api/users/user2@example.com/follow
echo ""
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer 7bb5fe3d-d9d8-49b1-aa02-f3b1a0c53432" http://localhost:8080/api/users/user3@example.com/follow
echo ""

# User2 follows User1
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer 9d1aa334-cb72-4156-aad4-4fc52eaaebf0" http://localhost:8080/api/users/user1@example.com/follow
echo ""

# User3 follows User1 and User4
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer 4b9d594e-f089-483c-8f84-6e0a1821b997" http://localhost:8080/api/users/user1@example.com/follow
echo ""
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer 4b9d594e-f089-483c-8f84-6e0a1821b997" http://localhost:8080/api/users/user4@example.com/follow
echo ""

# User4 follows User5
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer 51e2d1c8-0bde-4193-b200-23d06d2f470a" http://localhost:8080/api/users/user5@example.com/follow
echo ""

# User5 follows User6
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer ff7d9d71-87ba-4770-81ce-9a4c68e3c7e0" http://localhost:8080/api/users/user6@example.com/follow
echo ""

# User6 follows no one. No one follows User6 (initially from these commands).

echo "Creating content for User1 (Alice)..."
# User1 (Alice) creates a text post
# The ContentController expects a multipart/form-data request.
# This version sends DTO fields as separate form parts, based on user example,
# with values directly inlined in the curl command.

curl -X POST -H "Authorization: Bearer 7bb5fe3d-d9d8-49b1-aa02-f3b1a0c53432" \
  -F "title=My First Post by Alice" \
  -F "body=This is the content of my first post. I am interested in A, B, and C." \
  -F "contentType=TEXT" \
  -F "topics=InterestA,InterestB" \
  http://localhost:8080/api/content
echo ""


echo "Creating help request for User2 (Bob)..."
# User2 (Bob) creates a help request
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer 9d1aa334-cb72-4156-aad4-4fc52eaaebf0" \
  -d '{
    "topics": ["InterestA"],
    "information": "I need help understanding advanced concepts in InterestC. Specifically regarding X and Y.",
    "urgency": "HIGH"
  }' http://localhost:8080/api/help-requests
echo ""

echo "Script finished."
