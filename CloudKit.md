## Hello CloudKit!

### CloudKit?
[Apple](http://www.apple.com/)'s [iOS9](https://en.wikipedia.org/wiki/IOS_9) [iCloud](http://www.apple.com/icloud/) backups are based on [CloudKit](https://developer.apple.com/library/prerelease/ios/documentation/CloudKit/Reference/CloudKit_Framework_Reference/index.html), a framework to support the movement of data in and out of iCloud storage. The underlying mechanisms at present are not well described. To this end, I hope to share some of what we discovered in creating [InflatableDonkey](https://github.com/horrorho/InflatableDonkey). Bear in mind, these are early days and we are certainly not experts in the field. I didn't really know what CloudKit was until the other week, but shh, that's our secret...

In short we'll look at Protocol Buffer messages, CloudKit authentication and a simple low level message request. 


### Protocol Buffers
CloudKit makes extensive use of [Google's Protocol Buffers](https://developers.google.com/protocol-buffers/?hl=en)/ [Protobuf](https://github.com/google/protobuf). In short, a nice mechanism for sending data from here to somewhere in binary form. This data is defined in [messages](https://developers.google.com/protocol-buffers/docs/proto?hl=en) which are similar to code books: that part there corresponds to this thingy over here.

```
message RecordID {
  optional String recordName = 1;
  optional RecordZoneID zoneID = 2;
}
```

Unfortunately we were unable to find any proto definitions for CloudKit. We had to reverse our [own](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/resources/cloud_kit.proto). In short this was accomplished using [protoc](http://stackoverflow.com/a/12378656), [iOS9 CloudKitDaemon headers](https://github.com/JaviSoto/iOS9-Runtime-Headers/tree/master/PrivateFrameworks/CloudKitDaemon.framework) and [coffee](https://en.wikipedia.org/wiki/Coffee). The raw Protobuf decodes, as we observed, mapped onto CloudKitDaemon interfaces (with the CKDP prefix). However not all Protobuf traffic uses all fields which complicated matters, but rather like a game of [Soduku](https://en.wikipedia.org/wiki/Sudoku) it slowly came together.

At present the reversed messages are limited to our core requirements. Not all fields are described and some are described with uncertainty. Please consider them to be in the alpha state.

Example [CKDPRecordRetrieveResponse](https://github.com/JaviSoto/iOS9-Runtime-Headers/blob/master/PrivateFrameworks/CloudKitDaemon.framework/CKDPRecordRetrieveResponse.h) mapping:
```protobuf
// CKDPRecordRetrieveResponse
message RecordRetrieveResponse {
  optional Record record = 1;
  optional bool clientVersionETagMatch = 2;
}
```


There are more [advanced](https://www.sysdream.com/reverse-engineering-protobuf-apps) [techniques](http://stackoverflow.com/a/14027080) that could conceivably work in the rights hands. If you are able to pull this trick off, it would be the way go.

## Authentication

**Phase one:**

Can be skipped if you already have a dsPrsID:mmeAuthToken pair.

- $BASIC_AUTHORIZATION$: [Basic access authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) using appleID:password.

```http
GET /setup/authenticate/$APPLE_ID$ HTTP/1.1
User-Agent: CloudKit/479 (13A404)
X-Mme-Client-Info: <iPhone5,3> <iPhone OS;9.0.1;13A404> <com.apple.cloudkit.CloudKitDaemon/479 (com.apple.cloudd/479)>
Authorization: Basic $BASIC_AUTHORIZATION$
Host: setup.icloud.com
Connection: Keep-Alive
Accept-Encoding: gzip,deflate
```

A successful authentication will return us a [property list](https://en.wikipedia.org/wiki/Property_list) of which we require the dsPrsID and mmeAuthToken.

```
dictionary={
	"protocolVersion" = "1.0";
	"appleAccountInfo" =
		{
			"dsid" = "254477268";
			"dsPrsID" = 254477268;
		};
	"tokens" =
		{
			"mmeAuthToken" = "AQAAAABVAq3bqAfqrAfXoo/ofoq+afRAo=";
		};
}
```
**Phase two:**

- $BASIC_AUTHORIZATION$ : [Basic access authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) using dsPrsID:mmeAuthToken.

```http
POST /setup/get_account_settings HTTP/1.1
User-Agent: CloudKit/479 (13A404)
X-Mme-Client-Info: <iPhone5,3> <iPhone OS;9.0.1;13A404> <com.apple.cloudkit.CloudKitDaemon/479 (com.apple.cloudd/479)>
Authorization: Basic $BASIC_AUTHORIZATION$
Content-Length: 0
Host: setup.icloud.com
Connection: Keep-Alive
Accept-Encoding: gzip,deflate
```
This returns us another [property list](https://en.wikipedia.org/wiki/Property_list), of which we require the tokens.cloudKitToken.
```
"tokens" =
  {
    "mmeFMFToken" = "AQAAAABWMJrcsD_PYDY0j6yCXJSGfoyHDdDPmud~";
    "cloudKitToken" = "AQAAAABWMJrThhocmcODBDYoF2m9QwdRW2Id3z4~";
    "mmeFMFAppToken" = "AQAAAABWMrnYpDMcWqJr6QmfqjjueClUhPwllLs~";
    "mmeAuthToken" = "AQAAAABWK8Dv/A8nhO04aD+jVzTpPgwO42tVBz0=";
    "mapsToken" = "AQAAAABWMJr5kE82fcZrfa8pjbpLXzht6-saIRg~";
    "mmeFMIPToken" = "AQAAAABWMJrIlvcxKQNTRBEcmWKgJ-C7SyB6liI~";
  };
```

**Phase three:**

- $BASIC_AUTHORIZATION$ : [Basic access authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) using dsPrsID:mmeAuthToken.

- $CLOUDKIT_AUTH_TOKEN$ : our cloudKitToken

- $BUNDLE_ID$ : required CloudKit bundle id e.g. com.apple.backupd

- $CONTAINER_ID$ : required CloudKit container id e.g. com.apple.backup.ios

```http
POST /setup/ck/v1/ckAppInit?container=com.apple.backup.ios HTTP/1.1
Accept: application/json
User-Agent: CloudKit/479 (13A404)
Authorization: Basic $BASIC_AUTHORIZATION$
X-CloudKit-AuthToken: $CLOUDKIT_AUTH_TOKEN$
X-CloudKit-BundleId: $BUNDLE_ID$
X-CloudKit-ContainerId: $CONTAINER_ID$
X-CloudKit-Environment: production
X-CloudKit-Partition: production
X-Mme-Client-Info: <iPhone5,3> <iPhone OS;9.0.1;13A404> <com.apple.cloudkit.CloudKitDaemon/479 (com.apple.cloudd/479)>
Content-Length: 0
Host: setup.icloud.com
Connection: Keep-Alive
Accept-Encoding: gzip,deflate
```

This returns us [JSON](https://en.wikipedia.org/wiki/JSON) data, of which we require the production ckdatabase url and the cloudKitUserId.
```json
{
  "cloudKitDeviceUrl": "https://p24-ckdevice.icloud.com:443",
  "cloudKitDatabaseUrl": "https://p24-ckdatabase.icloud.com:443",
  "values": [
    {
      "name": "com.apple.clouddocs",
      "env": "sandbox",
      "ckDeviceUrl": "https://p24-ckdevice.icloud.com:443/api/client",
      "url": "https://p24-ckdatabase.icloud.com:443/api/client"
    },
    {
      "name": "com.apple.clouddocs",
      "env": "production",
      "ckDeviceUrl": "https://p24-ckdevice.icloud.com:443/api/client",
      "url": "https://p24-ckdatabase.icloud.com:443/api/client"
    }
  ],
  "cloudKitShareUrl": "https://p24-ckshare.icloud.com:443",
  "cloudKitUserId": "_0805dbd7a502a2b59574fb646e452027"
}
```


## Down to the wire

Higher level CloudKit API calls are translated into lower level Protobuf messages.

Known message types include: 200, 201, 202, 210, 211, 212, 213, 214, 220, 230, 240, 241, 242, 243, 300, 301, 302, 400, 800, 801, 802, 803, 804, 900 and 901.

Of these we use 201-zoneRetrieveRequest, 211-recordRetrieveRequest and 220-queryRetrieveRequest in our super basic CloudKit [core](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/java/com/github/horrorho/inflatabledonkey/CloudKitty.java).

As an example, message type 211-recordRetrieveRequest will be described in further detail.

First we create a RequestOperation/ ZoneRetrieveRequest [message](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/resources/cloud_kit.proto):

```
requestOperationHeader {
  applicationContainer: "com.apple.backup.ios"
  applicationBundle: "com.apple.backupd"
  deviceIdentifier {
    name: "d60818a4-7965-461c-fdb5-a84dee761e06"
    type: 2
  }
  deviceSoftwareVersion: "9.0.1"
  deviceLibraryName: "com.apple.cloudkit.CloudKitDaemon"
  deviceLibraryVersion: "479"
  operation: "CKDFetchRecordZonesOperation"
  deviceFlowControlBudget: 40000
  deviceFlowControlBudgetCap: 40000
  version: "4.0"
  f19: 1
  deviceAssignedName: "My iPhone"
  deviceHardwareID: "C4A98552DEE308E124D12D3F24379830E9A542506816D99AB6CE552363E4423A"
  f23: 1
  f25: 1
}
request {
  uuid: "851abb80-f8ee-4891-aa4b-e11ed36e3c51"
  type: 201
  f4: 1
}
zoneRetrieveRequest {
  zoneIdentifier {
    value {
      name: "mbksync"
      type: 6
    }
    ownerIdentifier {
      name: "_0805dbd7a502a2b59574fb646e452027"
      type: 7
    }
  }
}
```
Additional notes:

- requestOperationHeader:
 - deviceIdentifier: randomly generated [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) per session.
 - deviceHardwareID: randomly generated per session.
 - applicationContainer: required CloudKit container id.
 - applicationBundle: required CloudKit bundle id.
 - operation: varies with message type.


- request:
 - uuid: randomly generated [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) per request.
 - type: as per message type


- zoneRetrieveRequest:
 - zoneIdentifier.value: requested CKRecordZone.
 - zoneIdentifier.ownerIdentifier: our cloudKitUserId.


- *not all fields are described and some are described with uncertainty*.

Our completed protobuf message is [delimited](https://developers.google.com/protocol-buffers/docs/techniques?hl=en) (multiple messages can be sent) and the raw bytes passed as post data.

- $CLOUDKIT_AUTH_TOKEN$ : our cloudKitToken

- $CLOUDKIT_USERID$ : our cloudKitUserId

- $BUNDLE_ID$ : required CloudKit bundle id e.g. com.apple.backupd

- $CONTAINER_ID$ : required CloudKit container id e.g. com.apple.backup.ios

- $REQUEST_UUID : randomly generated [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) per request

The url corresponds to our JSON production ckdatabase url.

```http
POST /api/client/zone/retrieve HTTP/1.1
X-Apple-Request-UUID: $REQUEST_UUID$
X-CloudKit-UserId: $CLOUDKIT_USERID$
X-CloudKit-AuthToken: $CLOUDKIT_AUTH_TOKEN$
X-CloudKit-ContainerId: $CONTAINER_ID$
X-CloudKit-BundleId: $BUNDLE_ID$
Accept: application/x-protobuf
Content-Type: application/x-protobuf; desc="https://p33-ckdatabase.icloud.com:443/static/protobuf/CloudDB/CloudDBClient.desc"; messageType=RequestOperation; delimited=true
User-Agent: CloudKit/479 (13A404)
X-CloudKit-ProtocolVersion: client=1;comments=1;device=1;presence=1;records=1;sharing=1;subscriptions=1;users=1;mescal=1;
X-Mme-Client-Info: <iPhone5,3> <iPhone OS;9.0.1;13A404> <com.apple.cloudkit.CloudKitDaemon/479 (com.apple.cloudd/479)>
Content-Length: 369
Host: p24-ckdatabase.icloud.com:443
Connection: Keep-Alive
```

This returns a delimited message response which contains the request information:
```
response {
  uuid: "851abb80-f8ee-4891-aa4b-e11ed36e3c51"
  type: 201
  f4: 1
}
result {
  code: 1
}
zoneRetrieveResponse {
  zoneSummarys {
    targetZone {
      zoneIdentifier {
        value {
          name: "mbksync"
          type: 6
        }
        ownerIdentifier {
          name: "_0805dbd7a502a2b59574fb646e452027"
          type: 7
        }
      }
      protectionInfo {
        protectionInfo: "a\202\00230\202\002/0\201\203\002\001\0001~0|0%\002\001\000\004 \313\341X l\242 1\236\202]\322\216*\323\v\237Y\234\376g-\350\230Dg\002E\2573\234\265:\021\215[\016t\362G\322\307o\33004\257J\270\232\241\376C)\333qv\267\002\214cR>\330i(\343)\332<\265?\346\036lpyt\235\\g\030j\\@\251\202\304\355\017\fG\241\240\201\246\020\235\267\026<\023 zc\'\257Z%o-\030\035c\325\277P\333k\241\t\233\330\273*wkam\337\207\225\237];=\037r\246U\241\365XIzW\036\372N\206\323\274fo\005\251g\337\250\v\032\310j\237\215\312\275\306\315\354\n2p\310\270Y{\355 \330\322\367\\0N\004\000\002\001\001\004G0E\002 Z\364GH\211?\017\271\317|\242.\225\305\025\324A\351o}^\326i\236\215\004\201\243\003\255\316\000\265\3677\200!\nq\340\210\f\037\232zU\26567\320k@\361\264\343yhO4bc\002\261:\206\227\221]:\362E\333\350\256\006H\277K\374X\366\260{\242)0\'0%\002\001\003\004 ?\263\205\266!\342\262\347\303\"\300\320q\253\365\251\311\330\371\033\210x1\027\251&\374\213\253\260\241\201\3250\201\322\002\001\005\004\201\3140\201\311\002\001\001\002\001\0000F\002\001\001\004A\004\225=D\375\233\304\206\240G\376,\226\246\374\225\322%\357\345\000\265\225Z\310B\261\321\326S\207\323\004N?\324SD\306\346\016\345\314\321n=\324\tA\304\216f\204\037lUf\241\362\233\226*\331\347:\211 ?\320\226\2613f\345\267\355\246\324\273\332\002!\000\255,}mA\311|\277l\245\313.7\216\210\222\304;\373\303\310\330*\v\350\257\200Ek\252)_\314\252\027Qe\275\271\006\207\216\004S\001\000\265\335\364,\337\272\253\002d\020\031\026mv@\313\200v}\356\364\312\021i\004 >.\034\005\321R\316)\373%$P\241\235\377\235\221\364[\006\302\322\027\263\365\244\031\272\221\017\251\222\242\004\004\002\255\316"
        protectionInfoTag: "B2FF308458669E5D82C067799683815FCD26E4B0"
      }
    }
    2: "\001\000\000\000\000\000\000\n\242\377\377\377\377\377\377\377{\343\225t\232\327\233A1\232g\343$\343\324\342"
    4: 0
    5: 12014411042
    6: 0
  }
}
```

Additional notes:
- result:
 - may contain error codes


- zoneRetrieveResponse:
 - uuid corresponds to the request UUID.


## Onwards and upwards

The CloudKit framework is fairly extensive and we only cover the small fraction pertinent to our project's requirements. Additional insights can be gleaned by running InflatableDonkey and examing it's output, along with the source code. Apple also provide extensive documentation for the outer [API](https://developer.apple.com/library/prerelease/ios/documentation/CloudKit/Reference/CloudKit_Framework_Reference/index.html).

As a matter of caution, the presented information should be considered incomplete and unvalidated. Over time CloudKit's inner workings will be extensively described and this tutorial can relinquish itself to obscurity.
