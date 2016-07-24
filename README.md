**Update**, 24 July 2016. 

I've ported over the [LiquidDonkey](https://github.com/horrorho/LiquidDonkey) selection/ filtering code. I haven't had a chance to fully test all of the parameters.

Concurrent downloads have been enabled, but the thread count is set to one until I've had a chance to test it. You can change the THREADS [Property](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/java/com/github/horrorho/inflatabledonkey/args/Property.java) if you want to play with it. Concurrency can be a nightmare, but from day one I've written the code with concurrency in mind. I'm sure some of you have noticed the heavy functional style and immutability. So hopefully any bugs will be relatively minor.

The error output probably needs attention. I know what the various errors mean but for more normal people it's probably an issue. However, at it's heart it's still a development tool that's aided me in unravelling the recovery process. With full trace [logging](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/resources/logback.xml) enabled, it doesn't just download files, it shows you how it's downloading those files.

**Update**, 20 July 2016. 

I've pushed the new XTS code. I'm not really convinced I have the AES-CBC/ AES-XTS switching fully worked out so please set the mode manually if you have corrupted files (--mode XTS or --mode CBC). The FileAssembler logger output tells you which mode is being used, so try the opposite. 

Please ticket any instances where you've had to manually set the mode along with a couple of lines of the FileAssembler output (please edit out your Apple ID and the 40 character length device hash immediately after it).

Now that the core functionality appears to be largely there, fingers crossed, I'll work on improving the user experience. I have various filters/ user interactive selection code I fully intend to ninja from [LiquidDonkey](https://github.com/horrorho/LiquidDonkey).


**Update**, 16 July 2016. 

Ok. So AES-XTS is a thing. It's also a thing that's been preventing InflatableDonkey from decrypting certain files ([issue 9](https://github.com/horrorho/InflatableDonkey/issues/9)). Newer devices upload files that are AES-XTS encrypted for which we have lacked decryption code.

To remedy this I've coded an [XTS-AES engine](https://github.com/horrorho/Java-XTS-AES) that I've inserted in the appropriate orifice of InflatableDonkey. Initial tests suggest that it works. Once I figure out where the AES-CBC/ AES-XTS switches live I'll go ahead a push the new code.


### What is it?
Java proof of concept iOS9 iCloud backup retrieval tool.

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
 -d,--device <int>         Device, default: 0 = first device.
 -s,--snapshot <int>       Snapshot, default: 0 = first snapshot.
    --extension <string>   File extension filter, case insensitive.
    --domain <string>      Domain filter, case insensitive.
 -o,--folder <string>      Output folder.
    --snapshots            List device/ snapshot information and exit.
    --domains              List domains/ file count for the selected
                           snapshot and exit.
    --token                Display dsPrsID:mmeAuthToken and exit.
    --help                 Display this help and exit.
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

List devices/ snapshots.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --snapshots
```

Download the first snapshot of the first device.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --device 0 --snapshot 0
```

Download jpg files only from the first snapshot of the first device.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --device 0 --snapshot 0 --extension jpg
```

List domains and the file count for each domain, then exit.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --device 0 --snapshot 1 --domains
```

Download all files from the HomeDomain.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --device 0 --snapshot 1 --domain HomeDomain
```


For further information please refer to the comments/ code in [Main](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/java/com/github/horrorho/inflatabledonkey/Main.java). Running the tool will detail the client/ server responses for each step, including headers/ protobufs. You can play with [logback.xml](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/resources/logback.xml) and adjust the Apache HttpClient header/ wire logging levels.

[CloudKit Notes](https://github.com/horrorho/InflatableDonkey/blob/master/CloudKit.md) is new and describes a little of what goes on under the hood.

### Python
[devzero0](https://github.com/devzero0) has created a [Python implementation](https://github.com/devzero0/iOS9_iCloud_POC) of InflatableDonkey, but at the time of writing needs updating.

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
