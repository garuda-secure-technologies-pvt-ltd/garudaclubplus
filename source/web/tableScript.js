var floorId;

function saveFloorId(id){
    floorId = id;   
}

function getLocation(place){
    window.location = 'sec/showPlace.do?id='+ place + '&floorId=' +floorId;
}

