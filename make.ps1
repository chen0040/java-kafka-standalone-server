
$profile="local"

$currentPath = $pwd

if($args.length -gt 0) {
    $profile=$args[0]
}

Invoke-Expression -Command:"mvn -f pom.xml clean package -P$profile -U"

$proj="java-kafka-standalone-server"
$source=$PSScriptRoot + "/target/" + $proj + ".jar"
$dest=$PSScriptRoot + "/kafka-standalone.jar"
copy $source $dest

$client_path = $PSScriptRoot + "/java-kafka-client-demo"
cd $client_path

Invoke-Expression -Command:"mvn -f pom.xml clean package -P$profile -U"

$proj="java-kafka-consumer-demo"
$source=$client_path + "/" +$proj + "/target/" + $proj + ".jar"
$dest=$PSScriptRoot + "/kafka-consumer-demo.jar"
copy $source $dest

$proj="java-kafka-producer-demo"
$source=$client_path + "/" +$proj + "/target/" + $proj + ".jar"
$dest=$PSScriptRoot + "/kafka-producer-demo.jar"
copy $source $dest

cd $currentPath
