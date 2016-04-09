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
    echo 'Conectado satisfactoriamente';
    }#end else
    mysqli_close($enlace);
    ?>			