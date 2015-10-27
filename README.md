## InflatableDonkey
iOS9 iCloud backup retrieval proof of concept development tool

### What is it?
Java playground/ proof of concept command-line tool (currently a work in progress) to demonstrate the key steps in recovering iOS9 iCloud backups. It does not and will not offer a complete iCloud backup retrieval solution. It's primarily intended for developers or otherwise generally nosy folk.

The tool itself logs client-server interaction including headers and protobufs. Along with the source-code this should assist in developing/ upgrading existing iCloud retrieval tools to iOS9 (all those iLoot forks, I'm looking at you).

The CloudKit API calls are largely there. The cryptographical aspects/ key retrieval remain problematic. See below.

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

### iOS9 iCloud retrieval progress
Postulated steps and current status are as follows:
  1. Authentication. Functional.
  2. Account settings. Functional.
  3. CloudKit Application Initialization. Functional.
  4. Record zones. Functional.
  5. Backup list. Functional.
  6. Snapshot list. Functional.
  7. Manifest list. Functional.
  8. Retrieve list of assets. Functional.
  9. Retrieve asset tokens. Partly broken. EncryptedAttributes remains undecrypted. I suspect this contains the metadata required to manage/ decrypt our files c.f. [MBSFile](https://github.com/hackappcom/iloot/blob/master/icloud.proto).
  10. AuthorizeGet. Functional.
  11. ChunkServer.FileGroups retrieval. Functional on a private tool(1).
  12. Assemble assets/ files. Functional on a private tool(1).
  13. Decrypt files. Broken. Keybag retrieval functional but remains undecrypted.

(1) Memory/ disk caching, multi-threaded chunk downloader proof of concept. Alpha version and overly complicated for this tool, I'll knock up a simple equivalent later this week.

For further information please refer to the comments/ code in [Main](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/java/com/github/horrorho/inflatabledonkey/Main.java). Running the tool will detail the client/ server responses for each step, including headers/ protobufs. You can play with [logback.xml](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/resources/logback.xml) and adjust the Apache HttpClient header/ wire logging levels.


At present steps 9 and 12 remain problematic. If you have any additional information, we would love hear it! Please open a ticket and pour your heart out. However if you would prefer to remain under the radar, then email me directly.

**Update**, 24 October 2015. Good news and bad news! Good, steps 9 and 10 are functional. Bad, step 9 returns [encryptedAttributes](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/java/com/github/horrorho/inflatabledonkey/Main.java#L683) for files. Without this we do not know what the files represent, nor can we decrypt them if needed. Unless this is solved, it's potentially a deal breaker. It's possible we may be missing additional client-server responses. If anyone has any ideas I would be keen to hear them!

**Update**, 27 October 2015. General code clean-up. Protobufs more idiomatic. Keybag retrieval works, but it's encrypted. The search is on for [CloudKit Service key](https://www.apple.com/business/docs/iOS_Security_Guide.pdf). This should unlock the zone data in step 4, which should provide keys to unlock encryptedAttributes. I'm rather hoping the keybag decryption will follow in a similar vein.

So: cloudkit service key > zone wide key > file key

### Backups! (Solved)
The elucidation of client-server calls has been greatly inhibited by the lack of iCloud server to iOS9 device restoration logs. If you are able to assist in this non-trivial process then again, we would love to hear from you. Seriously, we would REALLY love to hear from you.

**Update** 27 October 2015. [Solved](https://github.com/hackappcom/iloot/issues/62#issuecomment-151144868)!

### What about LiquidDonkey?
Hopefully the remaining steps will be revealed in a timely fashion, at which point I'll once again cast my gaze over LiquidDonkey.

### Credits
[ItsASmallWorld](https://github.com/ItsASmallWorld) - for deciphering key client/ server interactions and assisting with Protobuf definitions. Please send him lots of cakes for advancing the project on so quickly.

There have been some contributors who, rather like vampires, prefer the cover of darkness. You know who you are and thank you!
