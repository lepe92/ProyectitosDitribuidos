<?php
  $servername = "mysql6.000webhost.com";
    $username = "a2811468_Transpo";
    $password = "comp15";
    $database = "a2811468_Transpo";
#recibir el id del camion
$json = file_get_contents('php://input');
$obj = json_decode($json);
$nombre = $obj->{'nombre'};
$correo = $obj->{'correo'};
$pass = $obj->{'pass'};

$enlace =  mysqli_connect($servername, $username, $password,$database);
if (!$enlace) {
    die('No pudo conectarse: ' . mysql_error());
}
else{
#echo 'Conectado satisfactoriamente';


if ($resultado = $enlace->query("INSERT INTO Usuario values('','".$nombre."','".$correo."','".$pass."')")){
   echo "insertado";
}
else{
echo "error";
}
}
mysqli_close($enlace);
?>