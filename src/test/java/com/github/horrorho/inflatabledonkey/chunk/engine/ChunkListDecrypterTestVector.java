/*
 * The MIT License
 *
 * Copyright 2016 Ahseya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.inflatabledonkey.chunk.engine;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Ahseya
 */
public class ChunkListDecrypterTestVector {

    // Randomly generated vectors.
    // 512 bytes
    public static final ChunkListDecrypterTestVector VECTOR_1
            = new ChunkListDecrypterTestVector(
                    "Vector 1",
                    false,
                    "01b10494169a749368e98d8efdf2a50f7c",
                    "023092c9f9ab92a9841efca0fb431a0ce73d7be5722e1a21c2",
                    "f65aa507381b30f40df155f972fe450e",
                    "01a81db3319113dc2158167fa9f1eb8db45cd6593d",
                    "IvItjbaXZ5+AE+WDTllJh/ZSAj3zo+MpdK16f+su36FdHP5DneYRhrCo9IGgzh5q"
                    + "iN5gR3crZyKVcB2E2pucW5jmlEFvE8EsraQe38qIwuyxIJ+NGLEEgFLLYmwC9i5Z"
                    + "47z2n1M7QwSNrUx3qeKIhK52+wKhTR2C07n6SYs/pGk52rGUttYKCPbH8G2CL181"
                    + "RKSapSWu2qES5vw9aMEdddIYO8FB2z4E/BMema/jWKLOKEr4oLDfCIOhjREn/han"
                    + "Itv+qFgMeOJ/Dk2Kqh6ArGtBlJnnC/BCWnixoxjmxK/l2HbiDvt5L2+y1efOEc6d"
                    + "2/Ch23LG3HUTA0Bx2HFQki1H+mBDG3eoq0E/62/MuoURxWY/4/C9xc6omghHqe1Z"
                    + "Ukb2wSVkZQAgVnM4pTaRRfNFPMeAoynzV+0GsvosEx2gfnjMJl4dWOU3fQiWIrmW"
                    + "ofXjl7mwmTXsYCOS+3hDJYakWzcVjERNDEMwlBp1bjEi/qh95kAUX/8A9ZlHszp+"
                    + "2ZyLFRd84joXZZUTBPPOD58OtyX95vBRB4ZFKkABYp1Ua92/o956M3JOC+AmaMwZ"
                    + "RJDWVYc/qrZT7Or0JMv4ySJLq9SBb337eDW49lybI6Jkpg73atiFTNOtnykBVXaY"
                    + "QgUYMoxd4REyAD5ar63eFwRrdbM1jLHKytFwH3boNAE=",
                    "D6jCKhS+mRLtLdP48I2rv5QNXaxlIU3dqxhLkwCdmqXFSrSNMAj/XuWwqk1f8WbW"
                    + "OWqQL0m5JH93TiuWrhyEoSXbRMdkGWF9d17bVuAAW+AUsrBRV+XSNYbyBtSDluUj"
                    + "krC66Xc9A5X8MN9EmHGrkEzn3gMpMWUQcsQYLZjoMST7/A5H9m56O6TmK9EpxROj"
                    + "a/1/oD0A9/9p62pU00HMKyWbgJqeIbhJsEfuANYKHQmwBQylaTbAK2aKGJ+CddPi"
                    + "uzAXx3Fif01ockZVbVuL/X+pExJ59vRpLVPnGOldAuC3O2LEkbhml5K+yh2cFSjx"
                    + "9TwGSfU7tx+++ICNib+UyAJFrbhwQ66ucsAilQJAyS3QcuGOpOyY8LWj4Qi5AJar"
                    + "3CaVEc04fA/wsYW0Uc8rU+DMPlej60u6qtOPEnusKj7+UjUupzr7ujLqTpCKqLQo"
                    + "XYJ7GMHR29uSRTYo4G6buC38DsEMC0oBvAX98b2Zndh1sYhf/DcTRRpE+2mjj51F"
                    + "mX1A5vnthnAB6l4g5celxHmAd/tBLNQfPJ2sXaJLDyKpyJ2NEV5/O5Yvrnp2yrte"
                    + "d6JkaayoI2oVEIMvn5H89SN/5vXG//ULyblT+pZJW/wyKuEc39xaghlC5R5MyKTa"
                    + "fSFy8BycKE2ytTAmEMvIPUfuy0wY/IU5mwDG69f0uOw=");

    public static final ChunkListDecrypterTestVector VECTOR_2
            = new ChunkListDecrypterTestVector(
                    "Vector 2",
                    false,
                    "01cd9f32e68ef9814333f2a5c179d26ea4",
                    "0244c3a921e2f7583c518cba007e14c9903ec9c1bb1f3af74d",
                    "b630ea66fa8145c3190c57a829613556",
                    "81d67f1759a4b139d37587b0ff6a5105b6738dd285",
                    "iLly+gzJGGLm4SDxpi/DN9Uj/hqt3kVfwTY3Xqr9RKagZblBg9vF4ZOLGCQMKiIx"
                    + "9hhCSM6EcOnKTGIkhMfZl+vDkOhNdPnQMQ37RH1uUlECrHKLelyjk2tmg1oD5Yz0"
                    + "r28oWtm1m/hvG3LQ2KLLwU/nF+CE06CAvjAFjJSggCqr2p0LLmtf8h4rrGvdDp4n"
                    + "AezLIV1SyRos4qyKuzlg4nEB38SlTxaDgliNJJUQVIn50B4cVuclHNsgPgiww8xJ"
                    + "rTFE3QawxocfX0lwsC97SDd8qTAGuS7I5sdaelqvvNOGWpgVDww6UsWYNuSBE3hH"
                    + "urj9uqDSryuEQU1BwBdNpTBJtyf9Sce1LPYiQmSh9i/3lKcZ0I1swoDYpm4QxJzX"
                    + "FwioBICiJzv/9tp+8bu56psWgoughioz3YfdcPOQtcD0EN+nZ39UJTxjko/6WBEN"
                    + "Nmhkiln7q/fE9H4tWCUlisDVFjgcSpiJq0n70w+esmBFDDNwy879lEe/CBj8llV9"
                    + "Sy9Ta8fcqVoGv7wUrCnAGc0H1VRQ7Ht5qovs5zT9nw71bLYV04yWkqcNvgCm1NlC"
                    + "/3XMMkQZFRIilnKu6F6IG5eskA6bbqJwp30bMN44K6QgNISmsrvgmtHPugLx+HZq"
                    + "C9R8GkKqkVQ9zAyyE17ifiuckmQCeoDD2WYgwCz/D8E=",
                    "pSMsyQj7ZtBwULltvoWf1nWhBpKr0/DKDADQVw6myeYc1G26sU7UrcEBHUdtqJ0V"
                    + "Qu/O+tYSMZZf2QoQqEyQbx7hj6UFmgA/n3LRy5uoLeJB6fXc7tBhSA35Tfao3dq7"
                    + "rM8h83zBu9BMORIYJWq81femnYbbPP7C/3qtVcTjfKOsa4aoAkcBg+FiCgKcCzcB"
                    + "slK4ga9e9J267ZabgacQAoAqitrwNQGindyez2GDS/lNFi993MVK32+FLDy1FsXs"
                    + "GERdm/ijO7OCZiNG5N7kVqkj/t4/3BGfNqDH7MTSV+FRoPMTfmmUEVy0maLDjusz"
                    + "HghlqVMLwwMt2dhSSzU8rEoNYlOu+T3L/H/9YZyb8hL0nXAohoypCdmTAhDdMvV7"
                    + "w8RnLgrrcBFEAQgELjszraNOZXo/48DrohLnDEMgigEwkoz0gifdQKIAATleoeXH"
                    + "L81fTblNC5gaUO4Gd3kUWpU6lqmfJxW8AHGVBaU/SWmjkrCmjI5EOJLuhOfYCBiR"
                    + "qNNj0FJMvXEo7Jez6ebTmgvw9divhPovFaY7LuLZzPJ4/eOfky+NrypQMdGD36Wj"
                    + "TdgtRl6FWbZ4+VSbA/mLOXzfSMsvePiF/V36qGoXqypjFBMejMV4imqpgfUzIeK0"
                    + "LGnkRE0cZheJtmOQdgI+C6td7ngCCIZU2hfyz3MeGKc=");

    // 1024 bytes
    public static final ChunkListDecrypterTestVector VECTOR_3
            = new ChunkListDecrypterTestVector(
                    "Vector 3",
                    false,
                    "014cf09532a20fca6e9a741d255ba13d58",
                    "02b84a72c64b6d9f664c6f1370f0a40714aba62382765a1b8f",
                    "d356c6f7d08e6d3394cc03b0660a0297",
                    "0109f7139bee724aba95729a13e6733efd3c2671ed",
                    "6og3tmN7X8XhuJxbIzh+Cthf+QHTvi7MJovp087dM+Op1bqkrDcyTi92myK2/rOP"
                    + "RB7GX0iC6H+M/+ygxUHHsMsLttmS9/TupIoIA+51+PuXW6O5mgnIFYmDAjADzo1U"
                    + "WbaPSe8GfScpvIZS6L8V9ye+kfo7eJjX3ecarDZNzJeA7zOS7J09HRWErgmr9+kq"
                    + "LozogsLli82+JFyAS9kLDQVC2t1SW+UIB0BNtYex/jnFSfPzmTg9sJ+svHI+GCzr"
                    + "LgSILEnATJhMBBH98I5S4BQN7xeUtunPZZjZ6XO3+aL+Oksb9lSWITMXnPEnfY28"
                    + "wNXbELGBFh7LPfGRmv+Aki1IwfsJgjUZJhpe8fVqnkPVEoRIk91n8htYJTCIGuZi"
                    + "nA2Z/ql3rxrb8Nv7HPUxV0GfInAlTno5es+/UFPU79QCz7kd2EYYBS0sCKOd0T5Y"
                    + "KriAD7SePKyA12xRlbuzuCkW01YenngSZt6w8AiEh7besvTuVjl4I0C6PDZDrR/z"
                    + "72WR6AWWSXWb/mhtsJ7Zd0z+TPA5XCNis8atwXAW/5JT2GtAH8JpkriaUGBjMth7"
                    + "nuo3KFajXG5d12bGsNT/TqLybWp9G/2CQn8zc0ykjc5hM0zM2o4Om1fBEmsPVMZp"
                    + "WYjXKlslKvYWOvOTQY+pGgaeKTeB8tqIcvBy2wakzEOcVKC+/n8LWtgrlua4zlOg"
                    + "XWZJqTUWlRJUyyWjj9baSoHGMNmjTn5JTmgI8x3oOSkQLMziW38o6jbeCjmmxs6G"
                    + "7U5S/u0RRcgyq3Yo7Zsvp1rasX85RO/fgeDlQyKtJAWvkTWMjWx4QA2IrF04wqZY"
                    + "t60Oz/0p5+7eWQ5l1NYyvGBCIPEl+te/7PrVLMPibR+aAILO15wDnSnmkk2DCFML"
                    + "Dr+aE9RdrXFTOcEEqBMVejq0vH5H9sGxDQinF6cWTXz5XCMVU7RlRyE4X6VlI137"
                    + "Ob5XfEBJ89T+RavAJ1A0g+lvic0iiDT+EH1UOipYRWFTRBDkK7Dqs6Zg0/J9iQZN"
                    + "Ob6v2Put67XK6muTNyZXFp/7BUBOrN4zl16eRfbqdvcLT+2IGe9Zv/qA1Ba+lnTY"
                    + "0Kj7OvwiioF7doVmCoHRsf5Yeqba1PMwo3d1kA/rjZsXA/E5DWD2+NWC/JnUWLVC"
                    + "HWWQ8hWIJL28eq3j8Z4Z/Pznp/D8SLlmbZlQlL/LsCPTJxlXCX6ZmVPsBfcFWX68"
                    + "eLJey6FYGR5UJG+dtwWIDiIBAxLxUzIwCbXKVm29WCepyXvR1pQ48kXfKY2zWapR"
                    + "01zsy5rMTkgYpmlNMazyn78YEtvY7ThKIe8BgEcGJMbbylqVPpk6Y+Rj6HoqayU+"
                    + "BdvFiKIcyyCjy9GS4Ko22w==",
                    "BhHZWO7QCEi9+uvgeGYGI9kW1m2beHFVA+lkzowWcVn9qn5Y5VcihiN6TVmZkkfu"
                    + "kg8AQWqDM4ePi4x1EfUFG3r3nfT3lqbGhfOQ/BynpujAbSXf4AteOUsJ9CvCYN8E"
                    + "UjD5mJIt4mPPY17M+PpK566QKfX73iqGU+adBGqdGgBGE1xrNBViuVCIeBJAvnCo"
                    + "ANSslpctjDz4CrrojAPG00aG5BUmOlCDG4mJZY6PtX3pufIMDt5/V8ZbMlOY62Ga"
                    + "1vURlMDIn4pZzBPfeoNxzAkb3J9EXrI1HgbGTTX4+3p4hn2toJK5VK7U+wXvPMo6"
                    + "waqVa0cDJ53HVbxXhmHZGlhTV2gcbfHPt79erDVgsfAH0n4A3WrQ2XOr2Pj8sI7g"
                    + "hU5wq8XEqkLFsw8UMXukQhGXmum4wPxLThqVyisGZ15kX9dm5fjCOjoJNI+7I85s"
                    + "EjBMUfAI34rhztHGWO+OarC3YJbHs63h6QoMQnSmEckuxdOyOmH9IKKnlrJlvbJT"
                    + "Nf1c26Z+nHkeXD9xbbsdOUmMDoeHuqhYVIiSEzw4pul/NRrYrUjIiQzL9CjJ5Csg"
                    + "QRrJilh/NTDZz4o72JbGMXzGlmsg0z+FY4ujyZvowZaOVLhs8ivcoiD5smGMmrvD"
                    + "aMB9Xqmd6C7w43ScyGp8xbbNF6vm/UYK+lxhmA4WRcy5ygQSz/sQno8AG5EUFrY1"
                    + "9valiviEid1IFWPC+Gi5N8I93EXcApT71JtvOesIP7urQmHdAtaUWNZLkuBB2qmm"
                    + "2wXllSgCa6DVo9ti+iOBoQPNfAiSwAtD+vxveJWxhGU+MiD21wOGkE0BCChXb1Vp"
                    + "FTHyoXkWnhj2ig8uZU+JWO4++lYPtqG3b0NR6BpfNCyNqlzraabLDLx44QX4jWdL"
                    + "FKz3ULJ4cg4RzMRUvkpT8z3Ths6RjH/kVa6R4L9GjnoyGk60giJXhvyeh6S5MKHa"
                    + "QYbtKxRVoKidg+//M045uvraNzKlrHSC1+YPs8jzEE0YHFzeWgVhWHcFr5OZFDDa"
                    + "o47EDmu7T+2Pnhv3QYZdniXL7mhwUSTK9P0hQllH9shnct8Wa/aw5/z9aRR7hns2"
                    + "B6KxnSrftkFDEsEBkEBFoHfibdEGfYT2zbJJ4+y6EMftX4VvkmoTQLKSKj2hKIHR"
                    + "aCeHT/5adt4y/2XyHFBOERkniEIalYFo9jNGC2zzzNfsHeAzpgl/5fcfY/fNDy+K"
                    + "vCH6fGhnBojwiQWSC4qKwlulVu2Y0jdJDFX4511USf3GeVwARDA0Ze351KnDZR7i"
                    + "uf4aVtDfYhKOOtvzoUKC9cg7q2CNO3oJCrdPjLzmn13IC9W1BsescBkR+7YK3FI6"
                    + "osADjgnQnXQUf6atvR5TCw==");

    public static final ChunkListDecrypterTestVector VECTOR_4
            = new ChunkListDecrypterTestVector(
                    "Vector 4",
                    false,
                    "017ca00e1427bb6ba479f15e4a4bdbf8db",
                    "0278186cd110aa09e6735b0a28eba84b06a9969dbec4fdbc51",
                    "604810ae2ac5bf7d58a40b59fcee86fb",
                    "81d4665136ee5a0c139abf84885e3f8243eee149ac",
                    "obtNnXm7Qy9Aq9K90owqYRV2J6e2YogTtlc2v6OX0QCw7G8E1b2oK89xI5zipB4l"
                    + "v9pDU+CspQCRDv5mTOjHs53uDLVKojsDT410z4TUAHJZsnkgLYj2qvAlsON58ykc"
                    + "DMMmXFBPbJ8F5bOkllTncJT+mEDVwxjMDWCAJnN6MN5ZT3uAO1srX5zHS2f5FXqt"
                    + "Q+Y2+XbZ7HT6rmu4dzUhXS3kr367PbFg8348WvhITnvcfIWXFtX+8IMVHL3PdVNy"
                    + "8YvsagDIbU08AAIY77gwO3rWrWBuNBytHrrBRWJGU5IyRhtNSdC2aGZuWG0dE4eo"
                    + "IMeOfqaOuIWHvxjQpsSAtcl97kMOjkT+I0WfTiZ0/mtzeO/3nDqbs7eiObBniEcZ"
                    + "E8Pd3tygrPmb7fpvRfYmoR3GIzu/qEIyAXInaaYhqVfLs0isEm1KnxV+bFN/2giR"
                    + "E0EcUkXYlr0jMuKnSUK9VVnSlpHTlb94bfpoHREhUc7nErQm+I2vIS7v+VYYop5Q"
                    + "awun4KJMx9s0TatZw1MU8ybbz6rWXFA3vkRsoQKxHu/cM8J/pblQIrLRtblMuccm"
                    + "6R6bWGBa6dPX/EmlnY1VfwXHsWJjkcWNMVYueNkFa6T0j1lNio2+2rON3B24Ox0r"
                    + "ioQtpWRspT6TDEX1wgVT7ES/2SvANS6WrWQuWF0LZ/eKQy0+5yySJloF0dNVis5g"
                    + "fY/BvXjpZ6XUZo41db7GWbvgFmclrAbkkuXpLI/YgGP8m3C0XJSDLu2my+Z2+Y2k"
                    + "5mWnaOQ/9+Et1efBhzWd8G7fEvzIEEULzfKG4Ln2Ze07WyVFmperHvBGtRFV5dFa"
                    + "hkt8oFI5Jix78ucJInxr64/CT+sstV7IoGeHofOamtoaLQZhkS0TDFCOhiLjGr/M"
                    + "UsFaQCz4P9I7BJgXmTduegZ8cNowcirnwL1/xhqOOOsBky3K2rh2p5fvf0OKDhoE"
                    + "G+wln4lsfifJ5Auz5ihQUi1tqQ+2oPDMfIPpzbn3RRbLFzBIseZtxHJmSQfQeXLr"
                    + "ZIssvwCKWk51ZTkI29bMo4oagJ48V7kkkYOKiAwSqoRJCvRJaZe51I/eLwTL7Y3D"
                    + "CcuyMSlzYH94HFSm0HcK8AcHyfddbNSqvUUTgODbpIJAYdFymDmgjD39c7pHpDhS"
                    + "wj5YIPLCODPM73CZipORQbCBMUcbtp8McCEXW3EtYv2DRnZecOTwqiJY4y+yeBa1"
                    + "hFIRnoiAV6d7F3ecjYIBja8FX3OTQV1+i+4tcN9IKxt63+Za4u7NBYxIdm/D9N4f"
                    + "NgdtIxYcfvuxeR/PjNP6hiY8D5ZlmLNGwFLCxp8LE1qJ/qsBII0/XepZ55aZBrou"
                    + "1NX9iX0xPUW9Aun4voB6Ew==",
                    "vS2gOjZTTEhgUOR2iv3JpFaje/PhhEoNK+VbwBMWg50LzrQS1kPY6LpcGaPuNA5u"
                    + "xN0uzWl/T/fNXgr50y43ZyvCn9ZAZoXGKA2x7oNkf76tSupQk6EGpX+LqNjTIGmj"
                    + "IsLSic1spdc4EtLPhm8FcjZJKaekKooEQenUe4RWlURm5t5hfge2SV9Ej2g3vwbC"
                    + "a36XxgONT3vnw5DEKSJsOwXQ+BOZ4JP1TTpJA4QPRCi1G81sHScGHx10SE4puiTi"
                    + "XSrv5YuZClht8psVH+45Q6jgw1I5j3vQN8InMM/123obkfpHyQfViVc/8DmaQiWr"
                    + "1CZTOKx0KxYLOuH5Tab4J04pUuFFg8GOn0PHFozc7Tuv9JN13E4531+SG6rReqcz"
                    + "0BT7WqkgQXPlCG1v8/EZYa1HKYCXES4Tz6ddG/Sz5bjspuqIo4QOGm9kJLDTqJdy"
                    + "I8iuaQSVj2BIyfrj9sgGAnP5AshEKKBy3NaseiKFj/VFWCdhQLAeOJinDjbjaVZJ"
                    + "Z1fB8N0Liu1I1D7RVCJ14DmxE2KTEuJmy7N9pIGV+hd7khbRfglud4458YT4+mdG"
                    + "EZBJtkYDP5KuLKQBd89/HAa91LRKVxNa5a/8NA/6KGUlk9wQaZFPOGzsSDv9Nbhd"
                    + "nbNZ/5YYyuH/3T6gMwqmuw5XUqbBP1O+0nUyVRRqeSrkUvSqSSOPx/EmLmagpDOv"
                    + "anXgc6LsQwfVjQQjVh0/7EwH/U4llj9uxEryncpoO8vtLprZWyLVp/9NKumS2Htc"
                    + "s0EpRG0L+k4PnuMFtpOuE6d25mPWihTttO95Ei8GLk9S1MLB+/nfb0bNG/C6RVkS"
                    + "oSHqzRQG7Dp5Ik9KX3VLaYbtyCzY36ogjjf6IGddIOm41OoiyW9nTZiQjH/NV9VG"
                    + "OBmq1uSCqMqraMasz5KSM6q35T2t5LGcI05e0jFbOwDLYWpRCga1oykx6pLpm6Z/"
                    + "r1n+2m/qtsYyXy64PjfU+uEfSLl9U5Q4Y5dTxyudiuOUujQjhssuaMrfLpVtsbHH"
                    + "i+mWX6o4ujklKynM/vmQecar8ck3vZYr0SI6wvhddNGbw6MYLBRG4YBQDjqI9ium"
                    + "2kNvDGSICr3Wb3pVx9S+Yc1PRzfFodnCBNyvPu5ShNl9522gwMb1OEIaXhOXQuaF"
                    + "vyFy1bSI5k8Xrpj1eTnZm32ZedPgaLy/MgG1t/TDzQ5f5A0w3JYJMIjMlJ991Oam"
                    + "KeKyvbG0hMj2p7WRzg8PKNsGp2e5oWe9q5YJj2+LI+YsYGUMZ8IIWPRPW7OIRw8K"
                    + "Z/SCA6V4L8Zd9/f3vDwnToeycUE7Wl0pVUhsWe6k/vdYtnP0p+A2I8TvQM4Sa67R"
                    + "8EU6qrM0cWS0DsuZ9Mf5Zg==");

    // 512 bytes identical plaintext
    public static final ChunkListDecrypterTestVector VECTOR_5
            = new ChunkListDecrypterTestVector(
                    "Vector 5",
                    false,
                    "01823455208ed4b8d912e3c2952d47d3aa",
                    "020e5dd712ebb16576bc5c7b04a765ec2057ab13122dced0de",
                    "672d3a229c5388e612ff1f219e8f7836",
                    "01ab763344d26327011932d8ab655466cff6cca022",
                    "Mka/dQ+2AHhCvlD0rqPmMdDgTJqFUb/cS+KXkN87SlwJwx/VKDx2e0EuPWt0hbk0"
                    + "HzJQPl8FCmDEddhEjpJxyaZS7ZoPg+fk0+hdwwAeaHYUNY8N/iqVtvT99hWcX4jl"
                    + "snQIRTHoq+cCXZM7yKdPcxaSl6GYiGBSsGrHXQX2RsZl+CsQcgf0Y5+m/QcXJCVj"
                    + "pDywS0BSDTOTOyDKTRzttoPFp/B7e6TtnNTGnVKNSyaePpwN/V7kBIjHQnLDO/jE"
                    + "6HJJ1F2tCY0/n4QiUkTKKU7bM/XKVWkQ59Ts6Ppc58PkgtgpT7ZaYW133LtyPjVO"
                    + "KFElLq9GtJ99t54S0mQAv/wrpiR9Cgd/F2c88aMqgH39rkFAJvkk7YCRdRhcNtoa"
                    + "yHpIF0LnLbE19LLwvdBz7rW4Se2pr8bAsj/lkBHbXSq7MCep9ucNtIeW0EPGsULm"
                    + "w8Qu0ZHUzWDCSy8MkvKFZCqJWhoiFeb7tsWDhkr41bwsADOGyQnsq7UGxzuhpWB0"
                    + "FpEpxSKK+Pw4Oiieq5N/o8xEuNlSB4QO/T0WS/LVHhUn3+6vYrzEP5c3I7ffkkrh"
                    + "3uQi2JaDV/eNVHJnGRXd/xACsU4xJy0oJSNl/thQHbz7ZfuU/llvuNZ9tYniBgWL"
                    + "eIe557GipcK8rhuIguamnLhPH250s0rPEWOuWx9awes=",
                    "MZzzdL9pqfR92RJwkVz3ht4Pkb8DRaFSJyoMkMKT4cuAsB7ouxmSuVxN7yGndfik"
                    + "x9MxHcuMmNIketfzTBXmZbTVkk6mLFlIAT2ZqU9uguP7rL6DyXQ+C5krFUHvk9cS"
                    + "Nx6tV7OazrP8788Puon7JNz2nkqU1ODehx6HOK2pkXPgLumNRhQznjSNFbvRpPUA"
                    + "CEGTf0efyJcbLlql0cdvcz5MyJst6QtTFYHAtuaqrOnnoz9V63Fw1h3mrF+aDoZ+"
                    + "HJjL/3+8HNAxbGt8C3n/fIfRTEs0YZUE5ONCwrrY95/YOeTv1S+WFaLuOtpj3aYS"
                    + "ln+vC/tDdobPFxR9n3IkhdruZw4Hw/4JMOfEoEXn+fzxNivyed3dfUOdh6IUsykM"
                    + "nqE1w0dCBx2M9l74QWIBJUpYhYp/jS0B+GKI8gUtd7HEJuFghwaFbH1ch59TyDes"
                    + "IF+etTHrnz5zVcGnuLs3LU5i0zuvLKcpQxdrEUJ4KEmPAnEuIUxIoelF5of+N8Db"
                    + "1pfZ1pdAbnuGpxQnGN39FH4W926xwoT45EtryoXqdhvAUak8G/e1sDqlcXD+rsOS"
                    + "b06cSJZ2uGF5vN65KrsmtDZ7+5W2e3gTLvt/8etvtaF0Heb4Q3nlESkGl384Vyjz"
                    + "WVg45haYeY/rMI6MhiYOU7srgz1NUS9m1hSJFDpdwrQ=");

    public static final ChunkListDecrypterTestVector VECTOR_6
            = new ChunkListDecrypterTestVector(
                    "Vector 6",
                    false,
                    "018f77c7cde1f4bb818e52f12fb0ea0b21",
                    "02030ce21f8c1a04f12eb1a2d125bfde374a1513a16772ddd8",
                    "8dfe48080d3432b7ee5ac0dc08a73542",
                    "81ab763344d26327011932d8ab655466cff6cca022",
                    "Mka/dQ+2AHhCvlD0rqPmMdDgTJqFUb/cS+KXkN87SlwJwx/VKDx2e0EuPWt0hbk0"
                    + "HzJQPl8FCmDEddhEjpJxyaZS7ZoPg+fk0+hdwwAeaHYUNY8N/iqVtvT99hWcX4jl"
                    + "snQIRTHoq+cCXZM7yKdPcxaSl6GYiGBSsGrHXQX2RsZl+CsQcgf0Y5+m/QcXJCVj"
                    + "pDywS0BSDTOTOyDKTRzttoPFp/B7e6TtnNTGnVKNSyaePpwN/V7kBIjHQnLDO/jE"
                    + "6HJJ1F2tCY0/n4QiUkTKKU7bM/XKVWkQ59Ts6Ppc58PkgtgpT7ZaYW133LtyPjVO"
                    + "KFElLq9GtJ99t54S0mQAv/wrpiR9Cgd/F2c88aMqgH39rkFAJvkk7YCRdRhcNtoa"
                    + "yHpIF0LnLbE19LLwvdBz7rW4Se2pr8bAsj/lkBHbXSq7MCep9ucNtIeW0EPGsULm"
                    + "w8Qu0ZHUzWDCSy8MkvKFZCqJWhoiFeb7tsWDhkr41bwsADOGyQnsq7UGxzuhpWB0"
                    + "FpEpxSKK+Pw4Oiieq5N/o8xEuNlSB4QO/T0WS/LVHhUn3+6vYrzEP5c3I7ffkkrh"
                    + "3uQi2JaDV/eNVHJnGRXd/xACsU4xJy0oJSNl/thQHbz7ZfuU/llvuNZ9tYniBgWL"
                    + "eIe557GipcK8rhuIguamnLhPH250s0rPEWOuWx9awes=",
                    "b01lev2ysLawInwyKt5q7+zZVtbbyNceA7qcTY7n68RzF7HpxR6n87qDnDfOcU0I"
                    + "zrFN3K5UwpQvE1PBx6dYYnbqoxQS9IXtJcdJ5KO+0dgo0D24lpjHuW5XmqqkHP89"
                    + "91YZOhhBR2CxTtArVfCAPhreKOjBn8OHDlxry40soMP+YDRUh5PpcBg5b8vKR1S/"
                    + "eXBDzgqtNzhEqIXP896A5kNkzW12W9JeMknv25g8Qp6eDbfr26SJ312bX1eDsPtS"
                    + "DVlfTqtLPH+FQ0SAdj0NfhKLQbW41CWRu9zuPBJGbuAfZzXqU0zIZkT9kZPTiH5C"
                    + "hCrng21coC9CLJ+X7yDYQCQON4VPTQWU6mgYmSLXTzj8WcpRJvS9KKWS3JW03LJ0"
                    + "JX9Tkf2Ma3+WMIANqWiuy7+9f0BaoE46HCeoTyw5nWW75P8rl73l9ADcs58WvKMU"
                    + "4f2mH1vgYv6j6I5IPmCg9BF5BpgdELv4oOOLHotLB2sDguVedZwXJtW3NJbYq2wg"
                    + "KU5KZkArTPHiogZch3otEd/LwAP1t7WYCKxK12p/QsKiJ+zugeiuHo4h9LliYn0K"
                    + "2GjpwqmpMO6b5N8ZZUXSlSA7aZzIi7GcAArG0Vmsr/DGiFlAjwbZAau8ywGLClvr"
                    + "lEC+v4FEL5XyLMqNKG0OZ55xnpDDo1rm2XA0h2ACw/Q=");

    // Bad vectors
    public static final ChunkListDecrypterTestVector VECTOR_FAIL_KEK
            = new ChunkListDecrypterTestVector(
                    "Vector fail KEK",
                    true,
                    "014cd92fff64da39b35e94a2bfaf3364df",
                    "029fb70825074b363b59cc7d2953bd9088c7a4c65819e7e0bc",
                    "00000000000000000000000000000000",
                    "0107c44326c081d785d2448f5861863103b7e3f00b",
                    "ffQ4srLP5WCAZ+5k8qqu/zRmKLlSIw0MzitoO3zkrPgVF2iQ30NAJthZfu1YpAvV"
                    + "43Ul4XIAa0jDJ4wMI7G6B0jR7HhxByA6LMQuhT41fg6fPPtAppjxZ3y2OKSkdIDc"
                    + "cmv3bwRqvymXgFx+d3bKQw2p1gxjrAthlp7bjz4QZuJ8EYFiMIqVIsMP7tzz3O5q"
                    + "yPtGUMPfPaiTH0xVvF1P/aetNtUtIfVU1ZDJc5Os63xdZ+hBGESDmOcuu27HXOvP"
                    + "sQH72ghyutmMsD2lbt1/Sh1Xmxs2/0A7ZHlcjt2AaDnqrQ6gT4vYXghZFkqYK4KZ"
                    + "ryVq1wfPTan2AAtiiwoiVjxyKcDAqGfL8TJEWQhxx26UFaVzeJhd4C3Z5SLrBjy4"
                    + "IwtnwYvIGS+BxG7KuexJX6ZdaV7QArBsr7vnZADzuL7rU5PFkeCDX3Mov1N2gZhR"
                    + "FrcsVe+rsRPzp34FGBIkTAQ8fDGQagMEQ9gaYpp9CU6UFVYV6LHfvhhOxBvF/WYU"
                    + "HOIXX+fUhCAFUd5nHZ/+ixr8JFZ8Cv6i7KJi/1nqVl4UqLmPGzPYdSOLa7rrj+9d"
                    + "bvkyZ/EneYnaJ0JjIqysulF9gyy0jElgP04z5YcavK9fqoQlypLoV1OTuBsVLHkW"
                    + "dNaOdtqLoh+1VYZreGQSizSByvja8LWdxCZFH+DVs0c=",
                    "bljxflUOrTHlSngmA+4CwHdpcN5OHgTzl6It8R67+7VxlNKPudypuZrJbeom3gCm"
                    + "ptMzwqsJ4Bp6wAgmWriv1kKPyrTkDvK0PxtcEy+PO2tGvzK2bJxqDp2RAoaPPl2/"
                    + "L1C/XJsI97bcsN32sRTvelR0CWsEzdo+XGkv58CjmPoc2wqc0156pq+i84YyzE3L"
                    + "1JdRP695nL0gk0hvJedal5MHYhQBcCAge7J31msgrXwuABGbQeEzNKeCimJueQU3"
                    + "a/4XlvHKvKx96MrFVy7nuniKLm9Gby/dPcyqJ2huC+ORTjqX95/4gebaxHTl2VXS"
                    + "HuKMgmhPNjGF/L7UgE0A4L2G+qmwNSCEnR58qy6mkQ9+YEpD7mmBvZWf8sX580IH"
                    + "5kHn+3AfgA9Kd8My9oIlb3c4qvo+nn0o+cUby2hxlkKpU3VbdRdtnFNVAiJLHySG"
                    + "zhoSvu3vuomWdrWopF5UuOH1J8YUpZysAIrigF8ZcAHtEZ5L765HSieTIcBQz06l"
                    + "5Tk+ao2delQmPrz2ZP/vJvoA3JEJFnzAfs/7GixIfm1hQsmoWzwuCpJ8SU7fZdZG"
                    + "ajf8dW1WaVtztJebOEQL1nIODMgQ7sqEZlQuswXcA161ZFPAqF3CtZfDezlQpS3o"
                    + "3AeQBhpCPCRP71Te7ibnoCNU8bcxZLpWdJpP8O4QZIA=");

    public static final ChunkListDecrypterTestVector VECTOR_FAIL_KEY
            = new ChunkListDecrypterTestVector(
                    "Vector fail key",
                    true,
                    "01ec4f898e542871b96cadaf5e4ba40470",
                    "02a9e8c32ee6f8bf8fcb4c419d672441b6595bb962307e7c70",
                    "85aa67ba738a9c411b3c3d89d5e8eab9",
                    "0107c44326c081d785d2448f5861863103b7e3f00b",
                    "ffQ4srLP5WCAZ+5k8qqu/zRmKLlSIw0MzitoO3zkrPgVF2iQ30NAJthZfu1YpAvV"
                    + "43Ul4XIAa0jDJ4wMI7G6B0jR7HhxByA6LMQuhT41fg6fPPtAppjxZ3y2OKSkdIDc"
                    + "cmv3bwRqvymXgFx+d3bKQw2p1gxjrAthlp7bjz4QZuJ8EYFiMIqVIsMP7tzz3O5q"
                    + "yPtGUMPfPaiTH0xVvF1P/aetNtUtIfVU1ZDJc5Os63xdZ+hBGESDmOcuu27HXOvP"
                    + "sQH72ghyutmMsD2lbt1/Sh1Xmxs2/0A7ZHlcjt2AaDnqrQ6gT4vYXghZFkqYK4KZ"
                    + "ryVq1wfPTan2AAtiiwoiVjxyKcDAqGfL8TJEWQhxx26UFaVzeJhd4C3Z5SLrBjy4"
                    + "IwtnwYvIGS+BxG7KuexJX6ZdaV7QArBsr7vnZADzuL7rU5PFkeCDX3Mov1N2gZhR"
                    + "FrcsVe+rsRPzp34FGBIkTAQ8fDGQagMEQ9gaYpp9CU6UFVYV6LHfvhhOxBvF/WYU"
                    + "HOIXX+fUhCAFUd5nHZ/+ixr8JFZ8Cv6i7KJi/1nqVl4UqLmPGzPYdSOLa7rrj+9d"
                    + "bvkyZ/EneYnaJ0JjIqysulF9gyy0jElgP04z5YcavK9fqoQlypLoV1OTuBsVLHkW"
                    + "dNaOdtqLoh+1VYZreGQSizSByvja8LWdxCZFH+DVs0c=",
                    "bljxflUOrTHlSngmA+4CwHdpcN5OHgTzl6It8R67+7VxlNKPudypuZrJbeom3gCm"
                    + "ptMzwqsJ4Bp6wAgmWriv1kKPyrTkDvK0PxtcEy+PO2tGvzK2bJxqDp2RAoaPPl2/"
                    + "L1C/XJsI97bcsN32sRTvelR0CWsEzdo+XGkv58CjmPoc2wqc0156pq+i84YyzE3L"
                    + "1JdRP695nL0gk0hvJedal5MHYhQBcCAge7J31msgrXwuABGbQeEzNKeCimJueQU3"
                    + "a/4XlvHKvKx96MrFVy7nuniKLm9Gby/dPcyqJ2huC+ORTjqX95/4gebaxHTl2VXS"
                    + "HuKMgmhPNjGF/L7UgE0A4L2G+qmwNSCEnR58qy6mkQ9+YEpD7mmBvZWf8sX580IH"
                    + "5kHn+3AfgA9Kd8My9oIlb3c4qvo+nn0o+cUby2hxlkKpU3VbdRdtnFNVAiJLHySG"
                    + "zhoSvu3vuomWdrWopF5UuOH1J8YUpZysAIrigF8ZcAHtEZ5L765HSieTIcBQz06l"
                    + "5Tk+ao2delQmPrz2ZP/vJvoA3JEJFnzAfs/7GixIfm1hQsmoWzwuCpJ8SU7fZdZG"
                    + "ajf8dW1WaVtztJebOEQL1nIODMgQ7sqEZlQuswXcA161ZFPAqF3CtZfDezlQpS3o"
                    + "3AeQBhpCPCRP71Te7ibnoCNU8bcxZLpWdJpP8O4QZIA=");

    public static final ChunkListDecrypterTestVector VECTOR_FAIL_CHECKSUM
            = new ChunkListDecrypterTestVector(
                    "Vector fail checksum",
                    true,
                    "014cd92fff64da39b35e94a2bfaf3364df",
                    "029fb70825074b363b59cc7d2953bd9088c7a4c65819e7e0bc",
                    "282b2d9abf3419eaf06a8e93a791bfdd",
                    "010000000000000000000000000000000000000000",
                    "ffQ4srLP5WCAZ+5k8qqu/zRmKLlSIw0MzitoO3zkrPgVF2iQ30NAJthZfu1YpAvV"
                    + "43Ul4XIAa0jDJ4wMI7G6B0jR7HhxByA6LMQuhT41fg6fPPtAppjxZ3y2OKSkdIDc"
                    + "cmv3bwRqvymXgFx+d3bKQw2p1gxjrAthlp7bjz4QZuJ8EYFiMIqVIsMP7tzz3O5q"
                    + "yPtGUMPfPaiTH0xVvF1P/aetNtUtIfVU1ZDJc5Os63xdZ+hBGESDmOcuu27HXOvP"
                    + "sQH72ghyutmMsD2lbt1/Sh1Xmxs2/0A7ZHlcjt2AaDnqrQ6gT4vYXghZFkqYK4KZ"
                    + "ryVq1wfPTan2AAtiiwoiVjxyKcDAqGfL8TJEWQhxx26UFaVzeJhd4C3Z5SLrBjy4"
                    + "IwtnwYvIGS+BxG7KuexJX6ZdaV7QArBsr7vnZADzuL7rU5PFkeCDX3Mov1N2gZhR"
                    + "FrcsVe+rsRPzp34FGBIkTAQ8fDGQagMEQ9gaYpp9CU6UFVYV6LHfvhhOxBvF/WYU"
                    + "HOIXX+fUhCAFUd5nHZ/+ixr8JFZ8Cv6i7KJi/1nqVl4UqLmPGzPYdSOLa7rrj+9d"
                    + "bvkyZ/EneYnaJ0JjIqysulF9gyy0jElgP04z5YcavK9fqoQlypLoV1OTuBsVLHkW"
                    + "dNaOdtqLoh+1VYZreGQSizSByvja8LWdxCZFH+DVs0c=",
                    "bljxflUOrTHlSngmA+4CwHdpcN5OHgTzl6It8R67+7VxlNKPudypuZrJbeom3gCm"
                    + "ptMzwqsJ4Bp6wAgmWriv1kKPyrTkDvK0PxtcEy+PO2tGvzK2bJxqDp2RAoaPPl2/"
                    + "L1C/XJsI97bcsN32sRTvelR0CWsEzdo+XGkv58CjmPoc2wqc0156pq+i84YyzE3L"
                    + "1JdRP695nL0gk0hvJedal5MHYhQBcCAge7J31msgrXwuABGbQeEzNKeCimJueQU3"
                    + "a/4XlvHKvKx96MrFVy7nuniKLm9Gby/dPcyqJ2huC+ORTjqX95/4gebaxHTl2VXS"
                    + "HuKMgmhPNjGF/L7UgE0A4L2G+qmwNSCEnR58qy6mkQ9+YEpD7mmBvZWf8sX580IH"
                    + "5kHn+3AfgA9Kd8My9oIlb3c4qvo+nn0o+cUby2hxlkKpU3VbdRdtnFNVAiJLHySG"
                    + "zhoSvu3vuomWdrWopF5UuOH1J8YUpZysAIrigF8ZcAHtEZ5L765HSieTIcBQz06l"
                    + "5Tk+ao2delQmPrz2ZP/vJvoA3JEJFnzAfs/7GixIfm1hQsmoWzwuCpJ8SU7fZdZG"
                    + "ajf8dW1WaVtztJebOEQL1nIODMgQ7sqEZlQuswXcA161ZFPAqF3CtZfDezlQpS3o"
                    + "3AeQBhpCPCRP71Te7ibnoCNU8bcxZLpWdJpP8O4QZIA=");

    private final String id;
    private final boolean bad;
    private final byte[] keyTypeOne;
    private final byte[] keyTypeTwo;
    private final byte[] kek;
    private final byte[] checksum;
    private final byte[] plaintext;
    private final byte[] ciphertext;

    public ChunkListDecrypterTestVector(
            String id, boolean bad, String keyTypeOne, String keyTypeTwo, String kek, String checksum,
            String plaintext, String ciphertext) {
        this.id = Objects.requireNonNull(id);
        this.bad = bad;
        this.keyTypeOne = Hex.decode(keyTypeOne);
        this.keyTypeTwo = Hex.decode(keyTypeTwo);
        this.kek = Hex.decode(kek);
        this.checksum = Hex.decode(checksum);
        this.plaintext = Base64.getDecoder().decode(plaintext);
        this.ciphertext = Base64.getDecoder().decode(ciphertext);
    }

    public String id() {
        return id;
    }

    public boolean bad() {
        return bad;
    }

    public byte[] keyTypeOne() {
        return Arrays.copyOf(keyTypeOne, keyTypeOne.length);
    }

    public byte[] keyTypeTwo() {
        return Arrays.copyOf(keyTypeTwo, keyTypeTwo.length);
    }

    public byte[] kek() {
        return Arrays.copyOf(kek, kek.length);
    }

    public byte[] chunkChecksum() {
        return Arrays.copyOf(checksum, checksum.length);
    }

    public byte[] plaintext() {
        return Arrays.copyOf(plaintext, plaintext.length);
    }

    public byte[] ciphertext() {
        return Arrays.copyOf(ciphertext, ciphertext.length);
    }
}
