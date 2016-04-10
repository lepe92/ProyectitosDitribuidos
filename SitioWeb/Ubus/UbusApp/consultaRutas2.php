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
#echo 'Conectado satisfactoriamente';


if ($resultado = $enlace->query("SELECT ruta FROM RutaUbus WHERE idruta = 7")) {
     while($obj = $resultado->fetch_object()){
echo $obj->ruta;
           
        } 
}
}
mysqli_close($enlace);
?>