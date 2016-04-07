**Update**, 7 April 2016. 

Ok! So I've had free time to work on InflatableDonkey. It's been painful, but there's been lots of progress. I've had to pull apart binaries to figure out the decryption process, which I hate doing. Like seriously, it's horrible.

At present the escrow recovery is working so we have access to decryption keys. I do have the protection zone decryption figured and I'll be coding that in over the next few weeks. Keybag/ file protection mechanics seem to be unchanged so that code will follow.

There's a lot of cryptographical code from a non-cryptographer. So feel free to pull it apart and suggest any improvements. Some of the code is quite raw and will make your eyes bleed, but it should illustrate what's going on. I will tidy it up at some point.

Sooo... what's left? Chunk decryption is the big one. I'm hoping it's a simple solution, otherwise it's back to pulling apart binaries. :'(


### What is it?
Java playground/ proof of concept command-line tool (currently a work in progress) to demonstrate the key steps in recovering iOS9 iCloud backups. It does not and will not offer a complete iCloud backup retrieval solution. It's primarily intended for developers or otherwise generally nosy folk.

The tool itself logs client-server interaction including headers and protobufs. Along with the source-code this should assist in developing/ upgrading existing iCloud retrieval tools to iOS9 (all those iLoot forks, I'm looking at you).

The CloudKit API calls are largely there. The cryptographical aspect is problematic but progressing.

### Build
Requires [Java 8 JRE/ JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Maven](https://maven.apache.org).

[Download](https://github.com/horrorho/InflatableDonkey/archive/master.zip), extract and navigate to the InflatableDonkey-master folder:

```
~/InflatableDonkey-master $ mvn package
```
The executable Jar is located at /target/InflatableDonkey.jar

### Usage
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar --help
usage: InflatableDonkey [OPTION]... (<token> | <appleid> <password>)
 -d,--device <int>                      Device, default: 0 = first device.
 -s,--snapshot <int>                    Snapshot, default: 0 = first
                                        snapshot.
 -m,--manifest <int>                    Manifest, default: 0 = first
                                        manifest.
    --protoc <protoc executable path>   Protoc --decode_raw logging, null
                                        path defaults to 'protoc'
    --help                              Display this help and exit.
    --token                             Display dsPrsID:mmeAuthToken
                                        and exit.
```

AppleId/ password.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur
```

DsPrsID/mmeAuthToken. Preferable for consecutive runs as repeated appleId/ password authentication over short periods may trip anti-flooding/ security controls.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar 1234567890:AQAAAABWJVgBHQvCSr4qPXsjQN9M9dQw9K7w/sB=
```

Print DsPrsID/mmeAuthToken and exit.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --token
```

With HTTPS proxy.
```
~/InflatableDonkey-master/target $ java -Dhttps.proxyHost=HOST -Dhttps.proxyPort=PORT -jar InflatableDonkey.jar elvis@lives.com uhhurhur --token
```

Selection.
For simplicity the tools operates in a linear manner. It will select the first device, first snapshot, first manifest and the first non-empty file.
The device, snapshot and manifest index can be specified, with 0 representing the first item.

Select the first device, second snapshot, tenth manifest, first non-empty file.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --device 0 --snapshot 1 --manifest 9
```

[Protoc](https://developers.google.com/protocol-buffers) --decode_raw logging. Specify the path to the protoc executable or leave blank to default to 'protoc' on the default path/s.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --protoc
```

Output snippet.
```
11:56:54.954 [main] DEBUG org.apache.http.headers - http-outgoing-1 << HTTP/1.1 200 OK
11:56:54.954 [main] DEBUG org.apache.http.headers - http-outgoing-1 << Date: Thu, 22 Oct 2015 10:56:54 GMT
11:56:54.954 [main] DEBUG org.apache.http.headers - http-outgoing-1 << X-Apple-Request-UUID: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
11:56:54.954 [main] DEBUG org.apache.http.headers - http-outgoing-1 << apple-seq: 0
11:56:54.961 [main] DEBUG org.apache.http.headers - http-outgoing-1 << apple-tk: false
11:56:54.962 [main] DEBUG org.apache.http.headers - http-outgoing-1 << X-Responding-Instance: ckdatabaseservice:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
11:56:54.962 [main] DEBUG org.apache.http.headers - http-outgoing-1 << apple-seq: 0.0
11:56:54.962 [main] DEBUG org.apache.http.headers - http-outgoing-1 << apple-tk: false
11:56:54.962 [main] DEBUG org.apache.http.headers - http-outgoing-1 << Content-Type: application/x-protobuf
11:56:54.962 [main] DEBUG org.apache.http.headers - http-outgoing-1 << Strict-Transport-Security: max-age=31536000; includeSubDomains
11:56:54.962 [main] DEBUG org.apache.http.headers - http-outgoing-1 << Transfer-Encoding: chunked
11:56:54.962 [main] DEBUG org.apache.http.headers - http-outgoing-1 << Content-Encoding: gzip
11:56:54.962 [main] DEBUG org.apache.http.headers - http-outgoing-1 << Strict-Transport-Security: max-age=31536000; includeSubDomains
11:56:54.976 [main] DEBUG c.g.horrorho.inflatabledonkey.Main - -- main() - record zones response: [f1: 20
message {
  uuid: "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  type: 201
  f4: 1
}
status {
  code: 1
}
m201Response {
  body {
    result {
      recordZoneID {
        zoneName {
          value: "mbksync"
          encoding: 6
        }
        ownerName {
```

For further information please refer to the comments/ code in [Main](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/java/com/github/horrorho/inflatabledonkey/Main.java). Running the tool will detail the client/ server responses for each step, including headers/ protobufs. You can play with [logback.xml](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/resources/logback.xml) and adjust the Apache HttpClient header/ wire logging levels.

[CloudKit Notes](https://github.com/horrorho/InflatableDonkey/blob/master/CloudKit.md) is new and describes a little of what goes on under the hood.

### Python
[devzero0](https://github.com/devzero0) has created a [Python implementation](https://github.com/devzero0/iOS9_iCloud_POC) of InflatableDonkey. Thank you!

### What about LiquidDonkey?
Hopefully the remaining steps will be revealed in a timely fashion, at which point I'll once again cast my gaze over LiquidDonkey.

### Additional notes
[CloudKit Notes](https://github.com/horrorho/InflatableDonkey/blob/master/CloudKit.md) describes some of the low level mechanics we have discovered over the last couple of weeks.

### Credits
[ItsASmallWorld](https://github.com/ItsASmallWorld) - for deciphering key client/ server interactions and assisting with Protobuf definitions.

Oleksii K - for cryptographical assistance, before he was mysteriously abducted by aliens.

[devzero0](https://github.com/devzero0) for creating a [Python implementation](https://github.com/devzero0/iOS9_iCloud_POC) of InflatableDonkey.

[hackappcom](https://github.com/hackappcom) for the venerable [iLoot](https://github.com/hackappcom/iloot).

[iphone-dataprotection](https://code.google.com/p/iphone-dataprotection/) highly influential and brilliant work.

There have been some contributors who, rather like vampires, prefer the cover of darkness. You know who you are and thank you!
