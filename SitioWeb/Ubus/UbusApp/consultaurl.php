<?php
  $servername = "mysql6.000webhost.com";
    $username = "a2811468_Transpo";
    $password = "comp15";
    $database = "a2811468_Transpo";
#recibir el id del camion
$json = file_get_contents('php://input');
$obj = json_decode($json);
$idcamion = $obj->{'idbus'};

$enlace =  mysqli_connect($servername, $username, $password,$database);
if (!$enlace) {
    die('No pudo conectarse: ' . mysql_error());
}
else{
#echo 'Conectado satisfactoriamente';


if ($resultado = $enlace->query("SELECT fotochofeurl FROM Camion WHERE idcamion = '".$idcamion."'")) {
     while($obj = $resultado->fetch_object()){
echo $obj->fotochofeurl;
           
        } 
}
}
mysqli_close($enlace);
?>