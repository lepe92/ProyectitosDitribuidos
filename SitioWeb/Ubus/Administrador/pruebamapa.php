<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>Simple Polylines</title>
    <style>
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
      #map {
        height: 100%;
      }
    </style>
  </head>
  <body>
  
  
<?php 
echo "Hola";
$conexion = mysqli_connect("localhost","root","") or die("No se pudo conectar con el servidor de BD"); 
echo "Estoy conectado a SQL<br/>";

mysqli_select_db($conexion,"ubus") or die("No se pudo seleccionar la BD 'mis_contactos'");//mysqli_select_db necesita dos parametros
echo "BD seleccionada: 'ubus'<br/>";
/*$consulta= " SELECT * FROM administradores  WHERE Administrador='Jalil' AND 	Password='12d345' ";*/
$consulta= " SELECT * FROM rutaubus WHERE nombreruta='Ruta 142 A'";
$ejecutar_consulta =mysqli_query($conexion,$consulta) or die ("No se pudo ejecutar la conexion a la BD");
echo "Se ejecuto la consulta SQL<br/>";
//$registro= mysqli_fetch_array($ejecutar_consulta);
$output='';
$coordenadas='';
echo '<table border="1"><tr><th>Nombre ruta</th><th>Tarifa</th></tr>';
while ($registro= mysqli_fetch_array($ejecutar_consulta)) {
	//echo $registro["nombreruta"]." - ".$registro["tarifa"]." - ".$registro["ruta"]."<br/>";
$coordenadas=$registro["ruta"];
	$output.='
	<tr>
		<td>' .$registro["nombreruta"].'</td>
		<td>' .$registro["tarifa"].'</td>
	</tr>';
	echo $output;
	}
	echo '</table>';
?>
    <div id="map"></div>
    <script>

      // This example creates a 2-pixel-wide red polyline showing the path of William
      // Kingsford Smith's first trans-Pacific flight between Oakland, CA, and
      // Brisbane, Australia.

      function initMap() {
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 15,
          center: {lat: 20.649773, lng: -103.412079},
          mapTypeId: google.maps.MapTypeId.TERRAIN
        });


        

var marker2 = new google.maps.Marker({
    position: {lat: 20.6521941, lng: -103.41},
    map: map,
    title: 'Hello World!',
    icon: 'bus.png'
  });


        var myLatLng = {"lat":20.657700000000002,"lng":-103.35185000000001};

var marker = new google.maps.Marker({
    position: myLatLng,
    map: map,
    title: 'Hello World!',
    icon: 'bus.png'
  });

var busCoordinates = [<?php echo $coordenadas; ?>];

        var busPath = new google.maps.Polyline({
          path: busCoordinates,
          geodesic: true,
          strokeColor: '#FF0000',
          strokeOpacity: 1.0,
          strokeWeight: 5
        });

        busPath.setMap(map);


  
      }
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC8z8IG_51YjAaDq5iNSEzfKn1BzgAmUEI&callback=initMap">
    </script>
  </body>
</html>