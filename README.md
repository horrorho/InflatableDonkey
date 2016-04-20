**Update**, 20 April 2016. 

Finally, like finally, it's slowly coming together... Well most of it. I've spent long hours over the last few weeks working on it and when I was in bed, my cat took over and also pondered the mysteries of iCloud's inner workings.

So! I've pushed another experimental build that will download backups. It's not been tested and large parts of the code base are mess. I'll continue to tidy up code and stabilize the whole thing. Logging has been set to info, just fiddle with [logback.xml](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/resources/logback.xml) to get the debug output back.

At the moment, the priority is refactoring old code and then I'll get work on additional features and unit tests. InflatableDonkey was only ever intended as an experimental proof of concept and it's a rather convoluted mess. I've tidied up the most horrific parts, but there are still phantoms and boogie monsters lurking around in there. 

On a more positive note, it was written ground up to be multi-threaded. Once it's stable and a few sticking points are sorted I'll switch it over to concurrent downloads.

For those wishing to play with it, please use the --token mode for repeated runs as explained below. Also please remember, it's an experimental build, not production code. It was pushed primarily to show off the underlying backup process, not because it was in a state of completion.

**Update**, 7 April 2016. 

Ok! So I've had free time to work on InflatableDonkey. It's been painful, but there's been lots of progress. I've had to pull apart binaries to figure out the decryption process, which I hate doing. Like seriously, it's horrible.

At present the escrow recovery is working so we have access to decryption keys. I do have the protection zone decryption figured and I'll be coding that in over the next few weeks. Keybag/ file protection mechanics seem to be unchanged so that code will follow.

There's a lot of cryptographical code from a non-cryptographer. So feel free to pull it apart and suggest any improvements. Some of the code is quite raw and will make your eyes bleed, but it should illustrate what's going on. I will tidy it up at some point.

Sooo... what's left? Chunk decryption is the big one. I'm hoping it's a simple solution, otherwise it's back to pulling apart binaries. :'(


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
