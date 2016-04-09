<?php
$servername = "mysql6.000webhost.com";
$username = "a2811468_root";
$password = "ornitorrinco8";
$database = "a2811468_distrib";

$json = file_get_contents('php://input');
$obj = json_decode($json);
$nombre = $obj->{'nombre'};
#$dispositivo = $obj->{'macp'};
#$nombre="Maestrante Jalil";
#debemos recibir la mac de la ubicacion y la mac del dispositivo

$enlace =  mysqli_connect($servername, $username, $password,$database);
if (!$enlace) {
    die('No pudo conectarse: ' . mysql_error());
}
else{
#echo 'Conectado satisfactoriamente';

$fecha;
$mac_p;
$mac_d;
$ubicacion;
if ($resultado = $enlace->query("SELECT mac FROM personas WHERE nombre = '".$nombre."'")) {
     while($obj = $resultado->fetch_object()){
           $mac_p=$obj->mac;       
           $mac2=$obj->mac;    
           if ($resultado2 = $enlace->query("select fecha, disp_mac from bitacora where persona_mac = '".$mac2."' order by fecha desc limit 1")) {
#si se encontraron registros devolver solo el mas reciente
                while($obj2 = $resultado2->fetch_object()){
                           $fecha=$obj2->fecha;
                           $mac_d=$obj2->disp_mac;
                          if ($resultado3 = $enlace->query("SELECT ubicacion FROM dispositivos_bluetooth WHERE mac = '".$obj2->disp_mac."'")) {
                          while($obj3 = $resultado3->fetch_object()){
                                     $ubicacion=$obj3->ubicacion;
                       }
                            }
                     }
     		}
        } 

$result = array();
array_push($result,
array('fecha'=>$fecha,
'nombre'=>$nombre,
'mac_p'=>$mac_p,
'mac_d'=>$mac_d,
'ubicacion'=>$ubicacion,
));

 
    /* liberar el conjunto de resultados */
    $resultado->close();

}
}

echo json_encode(array("result"=>$result));
mysqli_close($enlace);
?>			