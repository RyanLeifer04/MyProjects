# create a need
curl -i -X POST http://localhost:8080/cupboard/need -d @request.json -H "Content-Type: application/json"
# check for conflicts
curl -i -X POST http://localhost:8080/cupboard/need -d @request.json -H "Content-Type: application/json"

# get a need
curl -i -X GET http://localhost:8080/cupboard/need/1
# get a non-existing need
curl -i -X GET http://localhost:8080/cupboard/need/2

# update existing need
curl -i -X PUT http://localhost:8080/cupboard/need -d @update_request.json -H "Content-Type: application/json"
# check updated need
curl -i -X GET http://localhost:8080/cupboard/need/1
# update non-existing need
curl -i -X PUT http://localhost:8080/cupboard/need -d @update_new_request.json -H "Content-Type: application/json"
# check created need
curl -i -X GET http://localhost:8080/cupboard/need/2

# search needs
curl -i -X GET http://localhost:8080/cupboard/need/?name=Updated
# search specific need
curl -i -X GET http://localhost:8080/cupboard/need/?name=New
# search non-existing need
curl -i -X GET http://localhost:8080/cupboard/need/?name=Test

# delete need
curl -i -X DELETE http://localhost:8080/cupboard/need/2
# check deleted need
curl -i -X GET http://localhost:8080/cupboard/need/2
# delete non-existing need
curl -i -X DELETE http://localhost:8080/cupboard/need/2

# create a need to test fetching all needs
curl -i -X POST http://localhost:8080/cupboard/need -d @request.json -H "Content-Type: application/json"
# fetch all needs
curl -i -X GET http://localhost:8080/cupboard/need
# delete all needs
curl -i -X DELETE http://localhost:8080/cupboard/need/1
curl -i -X DELETE http://localhost:8080/cupboard/need/3
# fetch all needs when no needs exist
curl -i -X GET http://localhost:8080/cupboard/need
