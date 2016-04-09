<?php
$servername = "mysql6.000webhost.com";
$username = "a2811468_root";
$password = "ornitorrinco8";
$database = "a2811468_distrib";

$json = file_get_contents('php://input');
$obj = json_decode($json);
$mac2 = $obj->{'macd'};
$dispositivo = $obj->{'macp'};
#$mac="F8:0C:F3:A9:CC:E9";
#$dispositivo="D4:0B:1A:2F:DD:51";
#debemos recibir la mac de la ubicacion y la mac del dispositivo

$enlace =  mysqli_connect($servername, $username, $password,$database);
if (!$enlace) {
    die('No pudo conectarse: ' . mysql_error());
}
else{
#echo 'Conectado satisfactoriamente';

$claves = preg_split("/@/", $mac2);
foreach ($claves as $mac){
if ($resultado = $enlace->query("SELECT ubicacion, menu FROM dispositivos_bluetooth WHERE mac = '".$mac."'")) {
     while($obj = $resultado->fetch_object()){
          # echo $obj->ubicacion;
echo $obj->menu;
           #realizar una insercion con la fecha, el dispositivo y el lugar donde se encuentra
           #YEAR-MONTH-DAY H:M:S
         
           
           
$fecha = date('Y-m-d H:i:s');
#echo $fecha;

         
           if ($resultado2 = $enlace->query("INSERT INTO bitacora VALUES('','".$fecha."','".$mac."','".$dispositivo."')")) {
     		#echo "Insercion correcta";
exit(0);
     		}
        } 
}
    /* liberar el conjunto de resultados */
    $resultado->close();

}
}
mysqli_close($enlace);
?>