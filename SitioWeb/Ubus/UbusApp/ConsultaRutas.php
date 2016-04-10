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
          if ($resultado = $enlace->query("SELECT centro,nombreruta, ruta FROM RutaUbus")) {
             while($obj = $resultado->fetch_object()){
echo $obj->centro.'!';
                   echo $obj->nombreruta;
echo '/'.$obj->ruta.'@';             
}
          }
    }#end else
    mysqli_close($enlace);
    ?>			