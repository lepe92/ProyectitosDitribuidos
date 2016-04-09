    <?php
    $servername = "mysql6.000webhost.com";
    $username = "a2811468_root";
    $password = "ornitorrinco8";
    $database = "a2811468_distrib";
     
    $json = file_get_contents('php://input');
    $obj = json_decode($json);
    $nombre = $obj->{'nombre'};
$nombred=$obj->{'nombred'};
    $mac=$obj->{'mac'};
    #$mac="D4:0B:1A:2F:DD:51";

    $enlace =  mysqli_connect($servername, $username, $password,$database);
    if (!$enlace) {
        die('No pudo conectarse: ' . mysql_error());
    }
    else{
    #echo 'Conectado satisfactoriamente';
     
        if ($res = $enlace->query("SELECT * FROM personas WHERE mac = '".$mac."'")) {
    if($res->num_rows) {
        while($row = mysqli_fetch_assoc($res))
        {
           #actualizar el nombre en los registros
#UPDATE Customers SET ContactName='Alfred Schmidt', City='Hamburg'
$res2=$enlace->query("UPDATE personas SET nombre= '".$nombre."' WHERE mac='".$mac."'");
        }
    }
    else {
#si no hubo resultados insertar un nuevo elemento
$res2 = $enlace->query("INSERT INTO personas VALUES('','".$nombre."','".$nombred."','".$mac."')");
    }

    }#end if ($resultado

$res->close();
    }#end else
    mysqli_close($enlace);
    ?>			