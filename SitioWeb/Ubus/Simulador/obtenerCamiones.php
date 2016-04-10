<?php
$servername = "mysql6.000webhost.com";
    $username = "a2811468_Transpo";
    $password = "comp15";
    $database = "a2811468_Transpo";
     
    $enlace =  mysqli_connect($servername, $username, $password,$database);
    if (!$enlace) {
        die('No pudo conectarse: ' . mysql_error());
    }
    else{
if($res2 = $enlace->query("SELECT Camion.chofer,Camion.idcamion,Camion.capacidad, Camion.Nombre_camion,RutaUbus.nombreruta  FROM Camion, RutaUbus where RutaUbus.idruta=Camion.idruta")){
$salida= '{"Camion": [';
while($obj = $res2->fetch_object()){
$salida=$salida.'{';
$salida=$salida.'"id":'.$obj->idcamion.',';
$salida=$salida.'"chofer":"'.$obj->chofer.'",';
$salida=$salida.'"nombre":"'.$obj->Nombre_camion.'",';
$salida=$salida.'"capacidad":'.$obj->capacidad.',';
$salida=$salida.'"ruta":"'.$obj->nombreruta.'"';
           #{"Camion": [{"id": 1,"nombre": "mil vueltas","capacidad": 40,"ruta": "ruta42"}]}
$salida=$salida.'},';      
 } 
$salida=substr($salida, 0, -1);
$salida=$salida.']}';
echo $salida;
}


    }#end else
    mysqli_close($enlace);
?>
