<!doctype html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta content="width=device-width, initial-scale=1, maximum-scale=1" name='viewport'>

<title>Untitled Document</title>
<script type="text/javascript" src="jquery-1.9.0.js"></script>
<link rel="stylesheet" type="text/css" href="css/estilo.css">
<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
<script src="http://maps.google.com/maps/api/js?v=3.2&sensor=false"></script>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript" src="util.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<script src="js/efecto.js"></script>
<script type="text/javascript">



	//Icons
	var customIcons = {
      free: {
        //icon: 'bus.png',
        //shadow: 'http://labs.google.com/ridefinder/images/mm_20_shadow.png'
      },
      busy: {
        icon: 'bus.png',
        //shadow: 'http://labs.google.com/ridefinder/images/mm_20_shadow.png'
      }
    };

	//Popup dos markers
	var infoWindow = null;	

	//A visibilidade do mapa precisa estar global
	var map = null;
	
	//Este é um array global dos marcadores presentes na tela
	var markersArray = [];

	/*
	 * Inicialização da API de Mapas do Google 
	 */
	function initialize() {


		//Não vou explicar o óbvio!!!
		var myLatlng = new google.maps.LatLng(20.649773, -103.412079);
		var myOptions = {
			zoom : 13,
			center : myLatlng,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		}


		map = new google.maps.Map(document.getElementById("map_canvas"),
				myOptions);
				


		infoWindow = new google.maps.InfoWindow;				

		//Esse método eu criei para realizar o load dos markers no mapa
		//Execução imediata!!!
		updateMaps();

		//Definimos tambem execução com intervalo de tempo
		window.setInterval(updateMaps, 4000);

	}
	
	/*
	 * Método que remove os overlays dos markers
	 */
    function clearOverlays() {
	  for (var i = 0; i < markersArray.length; i++ ) {
	   markersArray[i].setMap(null);
	  }
	}
	
	/*
	 * Método que realiza chama o caminho do xml de dados
	 * e atualiza o mapa
	 */	
	function updateMaps() {

		// Vamos remover o que já havia de overlay
		// É possível implementar a remoção e inclusão seletiva
		clearOverlays();

var busCoordinates = [];
		var timestamp = new Date().getTime();
		var data = 'mapa.xml?t=' + timestamp;
$.get(data, {}, function(data) {
			$(data).find("marker").each(
					function() {
						var marker = $(this);
						var latlng = new google.maps.LatLng(marker
								.attr("lat"), marker.attr("lng"));
						busCoordinates.push(latlng);
						
					}
					);
		var busPath = new google.maps.Polyline({
          path: busCoordinates,
          geodesic: true,
          strokeColor: '#FF0000',
          strokeOpacity: 1.0,
          strokeWeight: 5
        });

        busPath.setMap(map);
		});

        
		//Aqui é o pulo do gato, que muita gente perde noites de sono
		//e quando você para para ver a solução, percebe que é tão óbvia
		
		//Quando chamamos um arquivo, o browser pode tomar a decisão
		//de armazenar em cache. Se o browser utilizar cache, as próximas 
		//requisições do mesmo recurso não batem no servidor.
		
		//Definindo um modificador único para o arquivo de dados conseguimos "FORÇAR" 
		//o browser a baixar novamente o arquivo.
		
		//Em java eu utilizo o header do http para dizer NO-CACHE!!
		
		var timestamp = new Date().getTime();
		var data = 'data.xml?t=' + timestamp;
		
		//Me guardo o direito a não explicar o óbvio, novamente
		$.get(data, {}, function(data) {
			$(data).find("marker").each(
					function() {
						
						var marker = $(this);
						var status = marker.attr("status")
						var icon = customIcons[status] || {};
						var latlng = new google.maps.LatLng(parseFloat(marker
								.attr("lat")), parseFloat(marker.attr("lng")));

						var html = "<b>Ubicacion "+marker
								.attr("lat")+","+marker
								.attr("lng")+"</b><br/>Manejado por "+marker
								.attr("chofe");
						if(marker.attr("chofe")=="Cantinflas"){
						var marker = new google.maps.Marker({
							position : latlng,
							map : map,
							icon: icon.icon,
							shadow: icon.shadow,
						});
						
						google.maps.event.addListener(marker, 'click', function() {
						        infoWindow.setContent(html);
						        infoWindow.open(map, marker);
						      });						
						
					//Opa... bora guardar as referências dos markers??
					markersArray.push(marker);
						


					google.maps.event.addListener(marker, "click", function() {});
					}
					});

			});

		}


		google.setOnLoadCallback(initialize);
	
</script>
</head>

<body>

<header class="encabezado">
<div id="logo"><img src="img/Cinvestav.png" width="80" height="80" alt=""/></div>
</header>

<nav class="menu">

<a href="http://jimenezlepe.comuv.com/Ubus/Administrador/Rutas/Rutas.php"; class="cursor"><li class="item_l">Rutas</li>
	    </a>
	<a href="http://jimenezlepe.comuv.com/Ubus/Administrador/index.php"; class="cursor"><li class="item_l">Inicio</li></a>
	
    </nav>
<nav class= "cuerpo2">
	<a href="http://jimenezlepe.comuv.com/Ubus/Administrador/Rutas/Ruta-300.php"; class="cursor"><li class="item_l">Ruta 300</li></a>
	 
	 <?php       
	$conexion = mysqli_connect("mysql6.000webhost.com","a2811468_Transpo","comp15") or die("No se pudo conectar con el 			servidor de BD");
	mysqli_select_db($conexion,"a2811468_Transpo") or die("No se pudo seleccionar la BD 'mis_contactos'");//mysqli_select_db necesita dos parametros 
$consulta1= " SELECT * FROM Camion WHERE chofer='Cantinflas' ";
$ejecutar_consulta1 =mysqli_query($conexion,$consulta1) or die ("No se pudo ejecutar la conexion a la BD");
$output='';

echo '<table border="3";class="item_l"><span class="derecha"><tr><th>Id Camion</th><th>Nombre Camion</th><th>Conductor</th><th>Capacidad </th></tr>';
while ($registro1= mysqli_fetch_array($ejecutar_consulta1)) {
	//echo $registro1["fotochofedirectorio"]."<br/>";
        //echo '<a>hi</a>';
        echo "<a><img src='".$registro1['fotochofeurl']."' alt='' width='140' height='140'/></a>";
	$output.='
	<tr>
		<td>' .$registro1["idcamion"].'</td>
		<td>' .$registro1["Nombre camion"].'</td>
		<td>' .$registro1["chofer"].'</td>
		<td>' .$registro1["capacidad"].'</td>

	</tr>';
	echo $output;
}

	echo '</span></table>';

      

 
   ?></nav>
    <nav class="cuerpo2">
	<div id="map_canvas" style="max-width: 965px; min-height: 600px"></div>
	</nav>


<footer class="pie">
</footer>
<div id="cogebox" style="display:none">


<div id="fondoNegro"></div>
</div>
</body>
</html>		