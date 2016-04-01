<?php
// API access key from Google API's Console
define( 'API_ACCESS_KEY', 'AIzaSyBTQ9yDPr0Gw1_8p85cEn_ZPFYEpPOxF8g' );
//los id a los cuales enviar
$idregistrado = 'fsnT95HPfs8:APA91bGZNVKepOm9aCSgADmSHbPVpcul-XWdCQVo6AnuaLbWlnSgwnK-SfG8Xlvb8FpM5I5rU0hfmeCRjUdHzh281jZ424-LoszKrCDlJjtgzPUmgs_XbEPKKYrsvgCzkCOvz9njvSOF';
//$registrationIds = array( $_GET['id'] );
$registrationIds = array($idregistrado);
// prep the bundle
$msg = array
(
	'message' 	=> 'here is a message. message',
	'title'		=> 'This is a title. title',
	'subtitle'	=> 'This is a subtitle. subtitle',
	'tickerText'	=> 'Ticker text here...Ticker text here...Ticker text here',
	'vibrate'	=> 2,
	'sound'		=> 1,
	'largeIcon'	=> 'large_icon',
	'smallIcon'	=> 'small_icon'
);
$fields = array
(
	'registration_ids' 	=> $registrationIds,
	'data'			=> $msg
);
 
$headers = array
(
	'Authorization: key=' . API_ACCESS_KEY,
	'Content-Type: application/json'
);
 
$ch = curl_init();
curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
curl_setopt( $ch,CURLOPT_POST, true );
curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
$result = curl_exec($ch );
curl_close( $ch );
echo $result;
