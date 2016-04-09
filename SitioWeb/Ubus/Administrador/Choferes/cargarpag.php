
<html>
    <head>
        <title>jQuery Load</title>
        <!-- Libreria jQuery -->
        <script type='text/javascript' src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
 
        <!-- Acción sobre el botón con id=boton y actualizamos el div con id=capa -->
        <script type="text/javascript">
            $(document).ready(function() {
                $("#boton").click(function(event) {
                    $("#capa").load('http://yahoo.com');
                });
            });
        </script>
    </head>
    <body>
    <div id="capa">Pulsa 'Actualizar capa' y este texto se actualizará</div>
    <br>
    <input name="boton" id="boton" type="button" value="Actualizar capa" />
    </body>
</html>