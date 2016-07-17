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
package com.github.horrorho.inflatabledonkey.pcs.xfile;

import com.github.horrorho.inflatabledonkey.dataprotection.DPAESCBCCipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ahseya
 */
public class FileBlockCipherTest {

    @Test
    public void testEncryption() {
        KeyParameter key = new KeyParameter(KEY);
        byte[] plaintext = Base64.getDecoder().decode(PLAINTEXT);
        byte[] ciphertext = Base64.getDecoder().decode(CIPHERTEXT);

        DPAESCBCCipher cipher = new DPAESCBCCipher();
        cipher.init(true, key);

        byte[] out = process(cipher, plaintext);
        assertArrayEquals(out, ciphertext);
    }

    @Test
    public void testDecryption() {
        KeyParameter key = new KeyParameter(KEY);
        byte[] plaintext = Base64.getDecoder().decode(PLAINTEXT);
        byte[] ciphertext = Base64.getDecoder().decode(CIPHERTEXT);

        DPAESCBCCipher cipher = new DPAESCBCCipher();
        cipher.init(false, key);

        byte[] out = process(cipher, ciphertext);
        assertArrayEquals(out, plaintext);
    }

    @Test
    public void testReset() {
        KeyParameter key = new KeyParameter(KEY);
        byte[] plaintext = Base64.getDecoder().decode(PLAINTEXT);
        byte[] ciphertext = Base64.getDecoder().decode(CIPHERTEXT);

        DPAESCBCCipher cipher = new DPAESCBCCipher();
        cipher.init(true, key);

        byte[] out = process(cipher, plaintext);
        assertArrayEquals(out, ciphertext);

        cipher.reset();

        out = process(cipher, plaintext);
        assertArrayEquals(out, ciphertext);
    }

    byte[] process(DPAESCBCCipher cipher, byte[] data) {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i += cipher.getBlockSize()) {
            cipher.processBlock(data, i, out, i);
        }
        return out;
    }

    private static final byte[] KEY = "Nagsisipagsisinungasinungalingan".getBytes(StandardCharsets.UTF_8);

    private static final String PLAINTEXT
            = "MSp36WNmx7HJmuGv6+a3gq54Y50ZjaFWDB7MLwgwDgmwf1lSQ5Wn8kQX2X6aHxiaf6ZZfqeTOiHNkUczpLIy8ETFHsr5fNapvxDH"
            + "P7+ApXBnMIQofJiLA1ApJhzUbiFFPN64K7JzRhrR1KBGZddaywpK0ZIWtBNRThqa6soDZjDKJJkl+7mBywyLs91XO7pY6FKcxPYP"
            + "u+WSWlzPtab+AQWZ37w4shMHnoV1v38hAyiKn7xcKfMDnC/tmrG0DJSqry/KyO0R9V7bhRif7MH6LR744Qy/NjvW11W9rEwHVkBc"
            + "kYBzu/sbI9MW+J8uLkLAz8VEGc4w1PljwfuOJERPfUIyJL9pMwHsxDjV2aF6Rx62VaXe1nS67oTwuVqkwF5Bnpc//t3nVxZ23t7a"
            + "x9Ow5rTenEfO6MGjXeq9xknbsg0VGXVYLrKsP2wfiyGcfHGc3v4xV9EUIYcMAjO8iSzchvKViUjk0zvKZRBFpW/uWypY5tGijq0E"
            + "EEy/fNxKdXH4a8e4H1jFvV73nOdMzalIWr6JitPQvCA45KXT+SFaOdBiS1yAwLZKZ8fkAXWBbdVdNVmaKBfnZvz7+b+YpsDvISJ4"
            + "5eKxeVKUg+H4b+bmqEdEg0rxFbW/3GHzVHSK9ZEg2ZwfEjT+oFfudO3/TQJJkHCwJRQe3dVfigZH/Tv5Q6g5PMSuRXBtaB4De95c"
            + "f4yUYbJrjObx8YwiKdLTRZy/t9lza6pwcIdDklA+OR9uiBQJserlzfrqcVRKY1DjSFPvcYS3z6Ilg6XsRC4wfages960F8LI1B6f"
            + "IE8f/JWTdRbrPOI3nAoFE+G5zFfAfhd7uJl9WHVagay/XToW8A3elqrFj9NnsA9TfpMbN/YlxlVO6nvwO8C4sKxDEBI93EL+Ie/3"
            + "5Snqa/DjpGnO259ElKMNV2h2mkbIwDUgFQ4KjH/rLVqGFw4euns97gtKGzlr3L+Vs3tvyZfORsp7MrWtbzWvxyHtPepECl4vUwMh"
            + "MnI/7K1UBe5FwEtI5FJAbwDKQW0y9kXx5GUc10D/HPiMJew8A+f4A4Nt2vgZ3CV7OqwG/t+YRC0atwnwzNw/FeplB/A8mZl5essO"
            + "pSERAs7o2OyXEW+hE0cbNlDtqVSPQPXQ8dargeLQizzAvBsK6oM0locWN9zQlDNHNdmcWNhr/tAfG/Mf6mJjG/WL2Mz4DEt3D5t/"
            + "ZgVx57JKXN5pSliLFDtfWmFX44u3ubJb/MxByb51VSveKHnhdJ8kClQ2vQ7rF4tfV1uJQWeCRWp6rV0DsZPz8z2QZBDCktaydIph"
            + "2QjOAJLS8sdFrQKRAhgBY206aWvifhgLKTGzAdrHP/mmbivEGt14HF1BISnPWEYHIbtTitefjoEhNN/Se84lihCooP4woa/u0yKM"
            + "z7U39BdfyvnfB01lM7TCPKABAbZkFzdSyKLR5HjyLBsgStUwgQeEHe15KU4g4qG3Rs5KC6yg+ihNUdfixYyUWxlZsGzfIYHLuInC"
            + "vBtsMjBStTUldPOQ7TSzI+q8d7sFa2AdWZtVdaSWZY93H1tqjfl/Zw5jO3njZFG/5D8L1DScEmbLnnODPleTzba2entD5WkOm+sK"
            + "kF4NSuRrVKZUjvjXGneQ7TDQvWkUdGzXvBnPpP/NFdJFQArPrYVnKHzMWUfrpKtYyqtAlHroVUaBOurUcwnbbIx3kKU1EY93BkGx"
            + "s545Fu+MLSeSrGGd3gnYHZJOxQqVA9EppklF8fy9XHWcdcHeEL457Tfx7TR6+ecM6k9CwWxIvAZArcwrJco3yjm2WGy8QGP3LDd8"
            + "ezm3x/beVoV19svZmvRWTsvhgrWI9t+HoRufR5xrVn4M/pXcXOGso46bsnKO2FGJ5ifkxRHwa5Jr9u/Qe8V8GpHw51h+XP8HiqZ/"
            + "KvoWLDCpb0LB7nkHvgE7JGMiXFWHo/v+2aMse1y6yEyP9GtrOg83PxRDmmyBmH12JQIpySLDTpyLNiJ5KomVXVhzqF1ml3/vfRSM"
            + "Nr29csncuHdnRlrbgOSvgsNgVAWnc3o17BEVTeO4VT5lXTBzrSSkZmoK1NuwSk+EuVPR9cg9lYlDnIsq1l7M02kBVv+l6c1pD2W/"
            + "R3MaqNcs9A6kgXessyQRF6QCMcghBZw8OBNxGchVWQBgRPL7GRoy8TZ32R7UarnxynE6nDKgGiFkzVzVH6QkGye1NF1gEedVNG1X"
            + "MaLt1nWzHmf4xvR8nm0CbIbdvqmD5bITuLR4qbMKmnvP5xOwQPdtkwrQmwlSYpXxGMZlegSl1r6SkohtWIgbZZgApqhfNFEWbvU1"
            + "biP0YC9NcnU4MbUcXqdrWdgxYp5LOl9IDYaKb2D0EJb9EfpDKobL7UXuKb0GVSIYmx+wopwoCovNZCC+Li2xvWGT7fqR3JM37gBY"
            + "Ls/VkuxM5G+hGJEZN1L4nVRO1pn5cYps6smC3EF7tl3cuUkGDYmX/WZ6sztWJASvpisDPzn7v7r4SoCOZ/Kal6oHkL/1WLKhw4t4"
            + "MtcPhYj8aCROBLX4O53SteQRWDgpXDn5cpMbpQF2vGRSLP9BW7dc2SVLrJL7DR8y78LB73u+2LXH+4cAqdyg1Qb+vmPk3TySVffV"
            + "uicjKt4S5pjat7OoKNsRaDCLheCPTElHKt2EJQQns6ojkV4raoQMxcJNaQsAvz85wXpKfZ40eGmiiuR3aRQ65WEw0Mae0VO3IW+f"
            + "nSD2dsg6rdOHgWTh3XIuQyeUSjbPwZ5DOetZ1XM6lT9RXPtSaYZD3ZWrl78QuJNu6tQueLPAmFBuUNBoKDv7e5wCuWGI6bmno1EZ"
            + "OkX4hz5wpNQ2YQeCUjKWvL3sdB5tdDURYImOdRsmhk1j0xVZWQ8yicbe/eBdFdH786CWQ1k2FldaQZo/O3yuJ+yTX2CWjaxKP7Dp"
            + "3mGcOzrrJg7wza/C1XeP18JUBjizUt6wdG32Zko9TaDJxchVfmFbaHFo4xtHj83vEC2LsYzHl5hzPprMcBOsx5RvMmSfjJC/VFZG"
            + "X15jIS4bTMYeZXEimb65syTWngujVEMoM8g0ZkbtiY2YPcXr4T3dJK2E2lh1uVZEIkvnS3nEl48r+aBG3dNQRtL/6uE7sXQptEJL"
            + "xYlOhDRG0u6LgcLsgrqFbfCyYRPTKy9nfdAXSvj9jIXIOWtGuxYh3eBj0IvpB5Rdvfc2DIalToTmmPTk8Whl0mBvf/mUGCMjS0qU"
            + "WXf8omafp7U1+NlQ7XCRL1tr1mKtwiYBR5+rd/5Sg1gy5Jr+7/xchgvQbR/NUtyWr/lqLFT9BOnE26fuPt2ui702okzj5TATdZNf"
            + "FwtFgFeXuxYCYksONTDd0RAkN4A+YXqHBGhSswzPRiCIVA6rhyusejQ+2FpmIjoYmIVXEKeIS7jwclvw5CvjgDhSQI+fyvwTqZoV"
            + "5QC+frD0rdK8odjxObISmYRRxfdk3pGpyPlFyrpaRulpDS2v78alHpYqXgaDPE4mu9AR5leoPDpXVxfsb9TXTE2bq24AZef4ZOX2"
            + "6r5Wk/LOHgyr3rYUXyZdFIR9ug8rojlzLmZd9iM2lmpwSRtiVB7AMRmnnM8WJhJkluRBz5srfkgzT+t41H+oxIaNfH0Pknz04YHk"
            + "NjzzYyPpGLNrrX+DBSq4gTB0y6pl2Ce7KEXz3s30KYpJ3BCgOMwT7XCDMmNyn1tISpX9zVGXXPI4+We0h/7hfu1wrf08qbDgymMS"
            + "wp6ldZk7QSFo0vLhinAc8ZIhEd4mjHcCE2xvWxPY7XZDE21Zg7b9ynWI7O/EgWAIT3u5f9mHtzJcoTANH3S8lhGVpdBJ9TUGEUSO"
            + "rjRbKDqG9BRdZSsWy7pCl58yIQP7V4bYTOH00dr/6rHmQMFq4xZMsB6JYfDK8P4JZY2kAPvIoyjudRdBWia32wqlEVjto3c3Jnxe"
            + "NjUnzoQfBglORhiVJFLmCDQ/D9ndjfMQDqsTdDkOZOzZQGuKpTOHoWx+gMWFOUSSxCBoUESJDPq5thHkl+/gQEUMLL9lQo3qEes+"
            + "4Bv0V7beMINpubGpqubnje5kf3+P4+m+F2gBqWsTMRPCQa0aDBWN1wHkoNWF6ls5xjL3Rwn2NNnv+WsCe/cfw+qBdTpBPyN+2Hxp"
            + "bxqThA6FMtEvOzu8GQYSO9KWh46YDGEpm1J5Gbb3hX6txJ9QxfOTUaQ+Mbl/Vv3C5VLxSOAedCTT4X/WYCP16pRpmmBflf7iCLnn"
            + "Wu5pev8dakUKahQhwa32co2EwTnUgWclRp6cNzsu9qrarzVzDfAka4wNd8mtqFFpYctlUlejFszgiUYcX+AVESftyZ7p6GbeoCJF"
            + "MAt2X38VA5iIqX7f6Ljs86hnWcIaHzqftnkiMblfBbdbZX3YMEif4O+QaawxfNVbqhfLmXNTCEkC69C48WOXOE5wWQIyTjVHLSoR"
            + "uAGAGFRIUNvFG2Qxvm5qt2/LF79ER51YoSOH/vdHAfQjcVHNL5hGIE/n6nAoaxWyTNsQ5N1wfPyXt1lZfWp3513CYM3t0ImnDq/s"
            + "5mRLUi1FJnx1ICbxfVw/p0hj63aRgMmwOG3i5nL6HOqW5eVpqn00nGGjSjFFd7Xo8aTzee32TJvxVIcK9FFkcujVnHhA9yNTn5M1"
            + "mXJppcged5rlzE2BrSCZAAOX5GxOKnQoqxaVEMKo4VNQV3WbUb59ZqxR+yd+r4YmhTZyGzYK3rjO7R9r8/h1cLf7EPa2YjpkRHPa"
            + "47HOhYIHox3YHh1HRy6Unv6YkAk+sJjiR5rAD13wfqzb8nA5erAinXoMXJY7wXHCwdR3OaerHYWR+DSi4VSSdrsLy0LuC+znKoGi"
            + "M7Wp/znQGzRk5Yutkx6NOsMSXSKjlAzcjbCartT8ci9oFgRK4gYcVh+BXB64uN31T4R1d/pVfOEmxikHajnOKFymBHVsJL/sZabO"
            + "ew98xKynlA4rBr6f3W5a+w0kqZoURnJlNJ6GMw0xdY+DOpWSR276c65dCMsX7sfGyQpQl2CutiZSr7gEUso6aSdFkL7WN5X0xTS2"
            + "yZtBKn628m583B1umy1KjFBX87o1D6smKgrEoFkGMzxQty9d7Ebeub89o/LJNa1z/j4f+J8WZ5fruijNp1zjb4SAsloaXRgb6Znm"
            + "qBZw/zQMK+kkt8p8C7Rg9g08iUL2jEuDR4/UKHCF7k4uR/ljR+vJZJVW7dJ/vZoS5zGfAyM3NLTPWfe5CJWb+ZzQfJCC7sNS/033"
            + "BD1KqY6wEoaW7rCaMXHnB9P0meTkq268rMLDTM1PapEe6rj1+wzkeuwHRKEK7rC5BFcscnXgHA74xQCY0NJJGyafIIZmgXyKuSI0"
            + "rj4GtrZYQifRUhxK22dxw2D4k/cnvZmEjREDJhqgA5vv7SPJGVTgX/HkI3UilC1aYsUhZfsMfZPZWMmZYv3uY9EbrqL0g+3qpn/0"
            + "+vOsAwLfDxWtE8A3+WMq2EHsKW5IiKMd1MtfwPgoaxNQ9cW6tRykHRyetOw8w3f51RoIre2Kcgd/FwllO5RcEn7h1SwA4ZlYH/yv"
            + "jjK3zCg4nV/LAhQkv9zEYovjaDUH7gTeX/n9kUfwRloI/aMrmrj/8tp1pK5OnbPbUiIL1bWHwU1UNMAOrxbGkBWeDMX0kBubjrx9"
            + "F8UROYIgRkPjiiQNX3DSf/EPv3rtIxMc5U27n76yJCZTPyCeqiNjvE7RR/FcA54TYrHblnXyCyK8ECORIec3678ZGZ/LI4uILaw0"
            + "DPFSOFrTWxP/KsYkDlnjEMf+YTVaPSRI7RO4lJo+rkMPbBVUtRYii9BJTrv5HR7zbVGGkMiw5GSshR90SgY2mD6nbwWJXApy2Qep"
            + "UZtrg3mD2K+Jyk8fxvVWMibUAuEd9oOuGL6QSWgbnB9001op9V3Zcs+8aEx4VMlEJ9jmqNQhanCEQ45trE13gkcjMe4UIdQyNj0+"
            + "pApKluV62xuVbsPm9uKHxcQkWrJFjDJqxuRMgtl5sQfoQcyNzkPFHA5EcaG6/wIgrSvhzrxKSM3KLC4SARO+ciKRwg5072JQLIzV"
            + "HptyqnHJN7QdGPrV6tQQaVbnH8/h6GWJlxDr4TKm7yx1LlB+PCdIWUATSur0DQp24PBPlCfuO3KrC/FYeTBgx9sM4f9m3yyMi1Vr"
            + "vl7ILx8pou+AmvOPQP8j2j3dZFONQCpzsk/Bk7s3EvMqXnPFBtJN+0paUZSu+b8L7uyaNHhtiDGfvAfwiK5rTC5rlisd0s5Cbfy2"
            + "wedFNW8r9jo6FA7YE9VZLitLkHm3ao4RDyy5I5ynFdkqDGgP+JfJbYMDz+Da5NtgJI5WNNulDyT0T53ILn3NKaeyFYDBOnRDhHs+"
            + "tRxhSiUHj/F/5kqM7wl1PPu9tjbR12Eula8Bn6bIxnQPX2W3MmzBJA875iZbT+K5dRXcP4biFjrnrT10uv3Bm2VJDm6RTEu/Aris"
            + "m8URrekrfL7C+xfBdch1IDF+U11Ug9j9qn7Ah3ZXgPHNv6hDsSo28dP4WchrMOZDs8gqIpDGtdf4spiEdZNhr/jBkuLhoBxKVG1V"
            + "g4Xx7yNK/xGn/jyJxcn1uoVtS8EGaavdHzedPGW6946+3uoFnxIgFxtLSm7zGDJWgGNqHrcsZ9VhpjCTIkq0OjoTQS987PFjv42g"
            + "bBSqZ+igKlAkp2MVE2PM180iEWHoK+XtmuEEeitq35EA3EEtC8QFPUdiiYHOto1zHE63HP679wLG20gZ6wTXHcKF4akb/aqX9pbl"
            + "54U8CC8cUYpBBIbpjTvaCdj3C9JLCEuIL6kUhvxdk80l8a7gFscZOTi7HOb81DJFzFoRNIyn8MbnuQHeMrPSLfZSGI8/eVpbRwmD"
            + "jV+UnIWnAzuZXcI5OCjadSlvHWsL0P5YvcEHC0CStrP77xSymtJExLBVn243eAdR3H0bnS9/TqIC69/mgP70F9+yd3aap6J470ub"
            + "3gdOVy2NuI1KWJPR/jRvQ+xomrgSGDbzy6Gl8DmHXCRZZROB0lb2P3ioQtTJa2GH36+w6HjKoOz647vAfhHgwrSP9jHAJ74N0dNV"
            + "NzAzXGnH48NVEX+Xmy95ASLPOq06SfKMJqmreJMk5i+TuOwfdW6F+oIZNs1D+id6skpFI7R08mQn9FhyaeILJ7W5Qmp45CrUQsMx"
            + "JgLPI7/0Ianx/Rfj88c7Ntb8odCiX97JAgj71hsjsZC/IYZjnwYaM4nPFsSK8s3xBOad4bNipicQFctlcKV+1FkA26cXnQ3vpjDc"
            + "JzdYLcjd5DIBOgLmBVKPMBr0RzSh5ZDvzUdTagRv4NW0wZaRH/RTY1yzsEXfyhlHQNFg1ko9WcVkIUew+BLR8rouQWRauERW+T8L"
            + "vyXa0PVtvc3Y7j0TxqMLCvdB1rgohGyyxAkGGdYSiT4jnxEgYCmzZ6EzzdA+KbjfZwwMpPGzHa4EaLi50MCfnpXe/KNzkW5QEAeZ"
            + "sdzt40a/5mOzyT+b0iuJIU3WHt+pDK7THF4eegv0/m81Nruv4M/IdsaysK/cdJCGk597f0TVt814FOjXbZOHlQlDjeeZ/iylUrDW"
            + "mrrM5w29hcxnWw5lcOLMjoVPJEeQd9/MVCnFO4k8TnZfblhUruMFw1q9LOs17IFsdh1VoSBeSi/e4TH4m8QYfna97HIXAUNgo1t2"
            + "lT+9cvu8McWobrWppUrJZSDbgx+6VriUJpmLg9C/y4cQFVqi9Pb9i3q6q4QIpBIW9IV3eH14CA9Pk4uxtsse0B+O+llTSwOtOv1K"
            + "Rvy+0A11MyC9acMTTxfiUtrCmQkTUWlooW9uo6rCSrHBc18LqbnvrIpIjvD6+E+5sZbAVumUMXvKMpWz8Wjx36ez5B9a2xYzAeic"
            + "KezUFfELkA20PT95mObQRexnK50IEs+RZiegCwNHFrVLsybc4mP1GMLPBotITY1eIObV9H39e846KYkiAAQR30sRw9MgV3H1gLOD"
            + "Btob2dpLuMqGK8g790MOCEQL509Dtn43xGcoiTv+z4EjGq1qHmggdfObuJ2bFJe7JYYg/T64pHpPrT7Q4Gdg+38XKAvSHNF5qJB0"
            + "CzwdGkIf1uV4OurhPJ7DomaVVHXAt/HyqZc2dSCcLYli8eQA3wAL7c9E98jOQYaDBLJ2cxE0xzdQJPInvt6v5w+DmEhfXIT4iYEN"
            + "oEMOnbKF2YqPH9qhZQ6SF/ky9bZj29XvAvZDsEUY7CDjPyvNJXLToVdEmn2wOtflb95JGjMZxlHVkVXXGcoS/sGQn9V3a2C1KtwL"
            + "YI/618gCECJXdf7NXXvlm/o/i6kvm4gP5GbX85C7Q4J6PfFt558NAIWoSgKwP+itvoOcKamI1IqVVFeUBZIdW1s5llqBJZJC4DBg"
            + "XAiFqvhpNgbrUtOAgoHIymFyiw1iOMYPDBDr9Gve1g1Bs0OQOOyt3VNUepBaYequb5pUaZJBbFWUTTe8ft5G/tJKgLN7BMJEDzyx"
            + "rSS9CsH3yfPvmgF1GlHwx/c6WJDmb8Pc3C9jUb+A8V04eSoZgxhPP2vX13EUGVVMPzCtqN9vzwjKKuy8pcZymiCBIC4ynD9iYsB4"
            + "smHl3evpAXvmXwmEScahBGhAB202zZ5XAnSFjV054YXROF4xup81iId52mL5WGrpPu/ynTONTLd0hlNwhBHyLydUtUZJKX84Kryc"
            + "oAayLiIroZUASfAh3AdIPxr+f6PpVZWyOhYRYZdTLujEcvEz+qDs9NhhqwwhXwPowEaVJTG8quj/F1R/8utYHJvRjAQPEo4afhuu"
            + "M0bI8C7rrTqIYwYBbdU1WxgAvkfBcs5gE+l7RXEDSU4cnq/vKibVp4CZm/SNlMv3aJf7tAulpqYdrdfFqKVyjfTPJuiG8DftJqgt"
            + "0FTJK0kG+bg6TX6rtO/WqA+FEFDUISJkb7gBnuK4HC1i75sZ/WSpbK53ZMLiyAFzf0FwUpR6Z0MauuaqdIJSU7Esi06NB9VzL/9Z"
            + "5QWKy0HadFUbxdNhwQto409Ia5sZ4av89sM4T45rycmjCmq9H+CdZAFKrMt/so4qU+fMb0GZhpAyFCMVYAvtp5vd0EieO8baj884"
            + "5EXBQWPTIoq0S9R0TJcDtmjy123eBGSU1FMx5OEDARCSfJI1g74rqREAub6briW4eS9tQgwol+LYWCLcrNBvPBOyEf/wWAO8UeoO"
            + "hxNyDdqaaddIlOXtTQrKsl1zHdNxPRpSjPi5Ifjd2a0tENXcyw7Savv9KxTunEl39StF/6lMCYM8m0zwyjpf2NwiO/WHXwSPBZqA"
            + "v6aEezMLIVYtcEZ6/GGcUZ8moFVbjUH8zHFCegYHkX+Pv96Iy6pYkvaP64tqHOxb/BCgVBwE4Oev6BgUUDgkVI6ZVZ/cRb3w0Pgz"
            + "/ufPXtKN+Nc9QJfAuuBo7peOqFFCgh2hcBvgKpuCB2K5CpuwcdvxsrICwJ6rTHcdKxRPwR1FHxKxw0I2cp+TpUZust3J6udFmg7Q"
            + "reLKz2vbejgF0NXoL5rzJ9qH31hEVFhkyb1FTSyhqkTh/LunzBmdYSjZJpNgaDSP46CnqGQ0Xy80BH/0grXiigmKj/2hN2Y5icja"
            + "+6vdZ5c0uQ6JVVzmRP+rEvvlirU5uJDUNJjumsMMxAhYg77YidmANpZ06FtHyZCKC4oXehoDBgGIE9tFv5kFutahVL4T6NjDUJ0U"
            + "0S3O6INPYRfWagdaiBEd/W5+51t9v/HsFrw3V/C7JcOSjiO5sOFLiShFSO05Yvc59iegpmMEk1e3EtnHtGh0+7PNOWEUlV8xqCfa"
            + "kXoAK9ZwlcxbDzbgal09d6gwWB9nguO1DfpaOxV13VkRZio9AbgojwYfTI1/ycttrapImNTvuZZDyZ9sAGVktalkzdlKzRk4wtW2"
            + "1PMw+8A7LF50tK1PrVmic+ECp5LQinmNpSZKNaPDXKOzptLe3e3uQzae2TFh+99Ob+1ZfeNYBS2Ta1oX4swTejV+K/4yEqgdssxr"
            + "6MmpALJp2pz7u6uJ4M3WoTU/uU3j0eT7c4ct+wX+m5JvDjY8Ij+iG7zFkXc8LYgCXon3ovTYRU6aOXQs1vEOVdMkSuQSNma9zeYK"
            + "989DhXyLtQdrReyS3cQ5U49/M0yjkNHhIs0sCOuLJYs4pfE0ogzG/1UQk8BInN3mDjPhHt5SBMoOfWuv6k0qfKk0kOJSehcIBaDQ"
            + "SSwjh4KFJrlynBR5KfDLkkfCW4O6QHCuP3H92+GXrc6qn2gSLLqh3tob7a/2uMWC/6PtwxbqGE7eUdEKWmZroeNfcxggCqsoDkrn"
            + "NXzx/2vJ8EVn091pyrlfBYPa/WgP+tYEBEwYkQBm5K032kgg2d3jKpksxM40rHX3lNo1hG54Mg700iQC2GBaCW2Fkk/eNlhVsMxM"
            + "OFL26gDDaH+ejur8JG2K8ZcmIKxkS8SeLEITT4+VVOI6J7vqRe2lHbq7Yews2n2Q4PXrWsaNM3ZMy3ihkuTYNhMJhG26uEeTwMLz"
            + "SSHia6x3CqvSFQhBrHCRqs7372JDOEu8ObRNGOkeFjjGr4lGeo6Obo+A1bJD1mG38bWBbokadzXnpWl1bN7zzafnIrXXC1LnSWXH"
            + "HTRsMuyhAH5sZCkSogfdvM+OEKTU6W4UkoonoewAES1uI8irXAaUS0JbVvsaoGaXLgJ+yXJY0mlsQRWd9s0q72bdILRge64Ix5uh"
            + "TJxQVuep5h21UMnvMwZIPDKkpbrwFkWrHxLDRJ5JyVrahVE4iu+1s9wbDoq81qjxlTgIBMVoByRY0F5HlAjFPjgbaVjpwQeUTgXv"
            + "mI6H5tVVGLuzvpbuZC+/he69uQGIUQajHud1rXxEQSrJPnc7K/xOYBE6sQg3p0YPZLUDiZ2R0UbAIHV65b2KYlIUOtbeQAmBP08b"
            + "2vDcj9tWJAXs+6BuJO94vkNhKp0AFnsVZf3BGlKxvg23XvsJey/fztR5pK+6XvpixMgqukidRIPJ8Vx4+l89EgAgQOhlFdLkJxkg"
            + "zhUvBAYZW1gnPXu6tMGrC7yNZQuKcoATpGbct75J6VnmrqTHZP4dJK16IIMJAYlVpnwW2SjwUyR2B5CjSjS/UHecwhfKsfvGGeko"
            + "UJkOxk6WMLT8dQaDpN4uUl9oy8RI5piYiJpynDK8C6ZmsAyWKRMRQvlLcD+LH7SVlWwsHOP6V3HbyKPYLeyPCutKi1dWSOo4V1oi"
            + "+ubBgQnIoGAqx2CeJ8Bff0FkgRmRCZxUdjaCBQXIpgO4RQ9nuCC/PirWKUdE4PzBO5lYiF7kjB/mWbPSqKAGQkkll1MgBHwKMflH"
            + "LclCUSl/4dRVwVk0GlNP8h/Gl6d7eH6tKVyD+dLKeBOPq/GkzRryWIEtkWMlawn5";

    private static final String CIPHERTEXT
            = "gk+CnYBeGxjQopHbicLLQWwGG9JMz5mdGLeg3FOhpzREdYgrPkh0u9nATfQ0FNseAT1Xw/u978E3XSzeI06mmfS5Vo9ZWrCgL47N"
            + "vWOXsvht36Df7xFmSv3/x/l14yFEJnZKzrXkq/az2qE2HISl8RXRi2VbstK0/Y89gxXrEEQMnFctNkAsvOMHFWMx8obYb7KDm+qJ"
            + "i4dEMPtVDCuVjtpP7sOLndSkCnBPwzMeV2SJdfGL3+/8ajeRZWIjSa1QNh9iJWYeSqAEePvMKtUCdnKK4iG2q+ckemI4W+0xtbNW"
            + "86Z42rMGtnuATywD+NphlxoKq9GFA3vxJxdhCdToApi8V6kuC1sdAYUmyeWRqNOy9DblYIU58EJORIMzyxn4IZ5Jizsixf2d6iiZ"
            + "LUnBmcuvGIEZ5DiKy+Fxc7VAGc5MMd75dIS7zfLN3nUd0On/BPX+NTs2/ayhSf4+AuyR/Qum69vt3HOqdIY8qpDuymtcjFmw2mrT"
            + "SiOtvN1uqchthqW0OHX0o4ckTdH+t3DhwvlesdCUAp0OI3RE6Z3kG9Gix8tjygucw9CvsvX4IKhNeh3MPCWGOWA3tsPEQiuywMWV"
            + "SnHrlkERLftzIy0eu5hT+Hyulo3AOBgh8gbxwTC4WrICt/weJ0AA47RATkv9L4+RxLenjWAa9lMXiaJ6jHuCftaSD0RW8yyW/7xH"
            + "iQkexbm0qkzSsh8M+IYgnFX5HARmXBbOWKNU1ZT+bIkCK3W/g1EEd6aWr7FX0JCNXKo2WyCXz7gNkGvFPPdOGFotMsQwt65/GDl/"
            + "fX2D7aVN2ivDnOl12pRPlJNqPTz7GtUiQWSfh++4LY4NwFYkdSUMXQgB3f01wRnVnXIg6z3g3TpzNVHORdLAI/KIWqw1kIgo2YF4"
            + "pC9MZrGyYzJ5kTaYlt7bKIxlZHdOGCa20X9r9p2l22as2eeMYOlbfNZa4Kw5z+nflTQq9oHVpfygwSTAvEg1daezWIQsX1e1G8sf"
            + "zqnrv9Elkb6oMTYieHEGDBF6Z8GAjFWlAAEy62VaYv2m01PYzeKJy/g4G7zB6XHKyQI5Q69rwFmuUWW4sLkG1/aLejaTD5EjSQll"
            + "HBIFJPCsU4pLyqNwIQidak1gj3oZhS5ZRBfoTdFytYuuhLTvrCOKbSrCnUERGK5wrHMwJwWGyWfyVKxZ65pbwsYqumuyT0Tkxf47"
            + "pncPlO1do6luuR1rggatzBN3LIPxytFg0NGvwAZpxWZRPWDr23l5PMkWUsoqQZLP/esKbg+rna9zLB+EVCqZSQTeFunBvpvITwkB"
            + "GRBZCvn+ghDyqgo5pqE1l9H/VLNzxF6aK5Ql84IRf6J6QwL4bmWz3tIk4Pf/r+YCncVYaI5DpLZG6OqLkHvNnyhym/ITq7+j/8MX"
            + "qMeqv7H/bRcikfOKKL8fSr1utxdftUmcO+bWXtKzwf+gBIhKkz8GBUjuQFlX3cI8Q+A8XdybNvyZFqviIR0wcsxGXKxrerXe7RSG"
            + "xVWrGjls2/yd5JkPXToOutRz6RDZkZMd4VPtw6bWaNG9zRTBNnolLvCshT1TC2NwLRSnKcxm8bVyzLX6fLsEudGpDzKJOQLg+ma0"
            + "XPCv3T5Qznk3jo79fYhFg7vwR3cK8eyPRHkkGTkNT+1BJJvyD0cbfLm+BnnhFKLgZ6O2jDBEySL6TQ7fpaKm6JncUYrhn7B8WOUB"
            + "KGHUAUtvZnIAFi6l+OqxDEF3HmIDLqTfootXN/pAu3VahKngMGFHpBj37o35ZHR2K5CKnxwwTly+rF6XolwfxHI5dZFD4C50ha2O"
            + "l+nWQZFt1IxEvFAqYFrMvwCPuKkAf9YCuLVOO4sDPurbZTE0tqIP9/1sp/6SIxRoPZ5kbvJd0Y6rkXvvw50jwp3EWYO55AYaU3F5"
            + "5PNOcSSFDOisKwwm6zyIr0RIqGC+8a77mMqCgPmQK6vm90W5oPtHlbNJBSC47XAsHSbRFeIHcdUZ+878lVOxoGPhDRcWC6zKuctq"
            + "kTPZzl/ul5tDbIgcd8kbsLkQE9mZ/9nShJqGuYjCIaxZl7UsgZXhoNokxkeJqAYhZch4ZcPtY5cRsOk19/VT3XawHuFKlRkBkVt6"
            + "Wojyfcsa08dF5yyM1tc+KknBSBkM0SAZy8ID6WuGWOa8BmapWF5A0ztXDkF+xn3AIYHUH6KctSaCA3OercnYS/Ng7EYQ92YVGUpf"
            + "vs8CjUcu2AQ+oez4FOgcz/A0jwFaJLEtUsk8w/HlTIoAaSLKDCPUrvTyEzgRnQRZpkiKtB9+1H8AoiUj1IzAHGjMcPQBMdAiSAO/"
            + "ZTtyVWSWH0EhRR+jYxyhCS0CyK/MLmsJrxMw8fc5rxRR6MtYozuXLl8z4zsrpX2TXZXEDqgLLI6EjsPzRGeLwGZfauw/bgLd0U5M"
            + "JP6yXb+EOSxzlxjIJznyqN2yMCRDzAcYwozXn02Alifol9EzQ+eCmCQ8WjaLwSFG9y79jDZjogRVAf0IDhS3rVJMi6dD6vtBLeKV"
            + "KFgoUaVthsqI46MpNQn7XrI8+jN3XFlZJaNlfanKO+E6c8Si/NSEzU4+y9KC1jqDHwz8+tSthAYO+kAnXlknDK3ELYLPA2kR2RFn"
            + "wfbtpyAxYP29tTud46SDsU2ZAEZzWxJQrBzbijuxZkYmlOqDncwkrKfa8l0b2hJexqEf5IkLi37rRtmcZeN0B8eaANLguxxZegTD"
            + "T9KSkTTrIzIBbGue5Xog6phMz8zRdS8aISvOboVZjQoOZR5rdp60xLoP1HFXmOMSv731MkD6K9cbBwvKB3sspiVbOG7NNwZTxF7i"
            + "xZT5VSLwDXAiMhS/OpiIi9c7yGR/+HQ04oB2RnYyzQGWvE69qPo4ykN+b946izuUDjYkkzQGDE6Ss5LMwWX74ryxyarj3jEbGNEM"
            + "TIrTYxfmx0s/OgLfvfLbz6kgpXVlkxb1YxYw5gragvdlf5cJa3dlkhyunpCGami/JfMIbpszjmWztI9BOErccDDJt/IvMTK66kYM"
            + "7Kq9EPvVgHQ4BWflwsrNledRKtWXoN6AdAf01l4c1QLvoJFc6jEmFTZhIDct/XDeKSM9b2TKyZOV3/q2Kj4kauWEgUdE0o+vRfsJ"
            + "ZJOA9l3lZp6Alw80jpzyUnztQSAKqdNFU5V8xO3q3k/i8Ilnd8zCX2eegYygUvF2UtnPf5akHQv2B55m9R170QZoxf0y+mnn8EIR"
            + "6x1dJNgtSEKw5W+htI0F2EXomJaA5UXdUSra1GW/jFXr0sl6JyhDvuyWVijKomPx97G243oACg8mzPnKkWJqogTvhCXsg6WNRswO"
            + "t7DSBfFCdTq1nQwzx13bl7esTf2gXbwIAAExGsGM9YFeV/RgJvt++9A4iRuCIQkxlzccaXfFipt47CusbGIeV7hwziA5DaeZT3Qh"
            + "efOQ5gZxNI8Nq6AxfMeeR5G/SMADS0B+CqG+jy4bJHtWWDE/PRbd/Lm61gR4WWs+Nt25GbuijwyJqHxdXfFVMYSQGozsi0KHOw/r"
            + "twOLjIUOgtlJf5NjOtNmdyk/h6Tp6o/g7pj8dtQVXHn1Rp2RfI4ylWbLBnNyyyFMoIkKzXZWnnLD2kE4v+EK4oG+GSykAIiDLRyK"
            + "Ujs+EGZ5iYnT7mzCwwGR+ZkkgZtIbJAOLD7cpD7VD6ysyQc5QOE2uKANt7z6vfh7ttvW0s8RD/dNEtiUULXWzKBvqPECSsM4HZ5l"
            + "M4brdBXZGlX6EZdDlAkcYyTO5Ctjjb/f5w65HoF768HGZSy/bWjcBL3mXLzApwlcifM2APgji8+DMhHMpVeudx1xN1Cg8IxIwmwB"
            + "faFDbAFHObJFCWbr61xU/+LXNlac+La880km/LQzbAhk+56PoSAJsUFg16VGbJoHcxvTRYP92nGL+Ialwd4F94OASJybNOnXKPgC"
            + "MLzimD04mueCTXTLG9NlZpa/N+Cf2wVfG/p82BBNYv3K2ZF+00MQauLYa2A5RvB1ERDMcsfGgJnJMrA9KcBHjmhTKfTl66BBh+9l"
            + "SQna2wF/X5xUJoKMHfE/YBmgHjHmH9xQs/uk1fVSQ8dIGiK1iyWhvuqK2rjKmu2R9EG06NxjGBjeTtVJhRfYq3jJ9FuCxHi63mQw"
            + "v+kF66A8rSOp1IJRoLl+uWxE/rUSyxJ06vAXI560pz7X9lrfCyT1B2Rb0GeqbrO9k3ZdDGPR+Uy9oBnrUrMdn79OaoVlmCsrXxUd"
            + "RQxADCASMzg1sUJT8NhVdTeRAymbM83fmWatSMUgVBVtn3g11tSOzlLFzpVV7SwdgIV4QqRAcYWWT3aZ+P6QBKE7IIatqoo1FawT"
            + "lX8D+q5edcl/98BEtf6nPHk+hQ+0olO2JYZpgMPg3o50tj4OuCXtQ7tk24dq1HT8+vSi7uYKwSbgSUDGKNTHqCn8AYLsLQQIrQL7"
            + "Ng2wFhp7nc/lDVdhlAKqEOQi38CiH4pf82Qb2sbZivKuxwtQ2BO8b+2NQZrS3vN7WTtpmttzbZGlBdg7/xY0GIlpb2nXjncwWxk1"
            + "bfC4gsgDyhzz5rhnzd4Bx1+uICHyV8gc9xuCbdtZXiWR29+1628XVgXfDI/AzhN8HEM+tLx7Z+SE8NkVGro8kguM0+4aN2axOnA2"
            + "zzmD9emq8OB7jsWW3zZQSPOJhvAK1NU9uS6UOgSAvPZciLYqc9z3z4EOYC2lrxakiwV+ZlCgXUONLSlCPEIH5WFS9L6G4bPX17Gr"
            + "9pv9tRHch7sQS3UEIszZgZAPAxlvSyBw/XWg6Tw13lLqkaxhZicaIfLoCwyRpc+p1knNd4dab1n7hgvpEcgYZwyt6Cxsh00fXfT3"
            + "dOvXnKEV0FcaOb+Dky9fb90HnfHXSdY84tD0+gB/2YPm10UMa9x2/+4JDIHgBUBcNmEwHsrL8JrYxBjqmM8BhZucJ2VUyMb4XyCM"
            + "+ouKcN2ttt9R/c2nojMAvFHCtfarmmL9oHqRG0mn2ZmOuOFrV7MD7XEXxiEM6yhc4mEq1tZsGappV5ZJzdFrv8f7713Mt0Tal4as"
            + "efbZ4iH9EpkZ1tmXKjI/t/jZ+kGRcSH0IkhyXh29ZLMXuRROTW4vj9dxOVSpUTJbUFFj2Ds3pa1F1NmCCOitX25QE8uMerIgjGmx"
            + "0ojatQCcfos1w4puVTneE1/xZ04wEiJ2JIqMA79zNkFa6wAK5lJ9SI7ENvbAY/yIoXu+Hzr1w69sULuX7INLq0F8R3zF2GCF5PQd"
            + "fxFxYt84wikiYj7yXzLB7pB+wHVNLeUW4IT+mNQIxEv8NqMs23ArpKlN3eGgj59mRVF0uLH13DaW+qpe4JKRtl76I3fG/FsV/yBa"
            + "G9Xv9I/Q1BaaaqoRSyuLRavPu18nqB23qu9X9Y1oNWorP803R49c0NJljZeorjoiUpsUraTsJhu1TBIokufUA2cDB7qh1jRn//wv"
            + "XVGklwPJU71eF1DfXurCzorPXlDh8+xVD0PjNoR0Y0IvGNhTFsMLZUPoVmnR+IK25zHnByAXb0//7POYs+Oi83b/smVqUTlhkQG1"
            + "p2j8hvgyh1FM/3DQOx3buUY5oBoEaE+k0+nswpZxP1cbTArWrbG+JMu3mssMHPkJD46KAhB6E/WDy2uEZTprBwzF02rbR2BNe2mQ"
            + "mEMnfi+HCAd4o5mLakj84miPsXX2gdCCoRn3bLEe2nxiupJM5ZHnKfRR79TyZHvFHmnYQuxQ08XFvNtB6HrE1RJq5A+tditxkRDh"
            + "OcZ17/QpRFuhhU/FMbuEbJ2/TEpEjdvbejorcbm8QVNsFiEg4qoScmrH1BuHJ7Z0uesRiseMfUyhXNdlUV6H+TJxdACZI8Ut5l/m"
            + "ey4hxs4fPFBmkj8T3wbj1TjM1Q/X3YHVGLF+hV2/cXkpCBjJukGWvDrmdzfpfxEtKyYEx2gXqJpG25XNqSM2W3Gyk/gI0E0rIRJO"
            + "RKAGBsKDVHKr6/bF+Kkdi/KOAVboAbIU6hTxuKp5teJG1VJd5+QiubpGoRKpookJS/phZMRmyo6lVa64mzPzayfuCyat36uTfR79"
            + "Tnq/SFZqfQ+sJuwp1XkyxXlbpu4vMYeR/atATM6iW/Rb9w2nLUZKbHoGu7Wp3k2FcLgbqZkwunZ/0tGuxe9JQG0ExFoKlOn6/HeM"
            + "rnWQ2m8Yzsx2dyj5i8hadOGZwjSYKoL74FMnY2JtccxZFdkrZRG06W3CnwTQuxdwkMyC8SWz41MHm9Mb8rFP66FjAx+jM4sYGbwS"
            + "3w+CSmg2FP3K67T2o6OftuB8TkGTTCn6H1PadTIBmRr1FLod4gN+onfsBTTvWCA/k24SLpByLOx/zqcUo8EHLWWoOV8wez8GSDVo"
            + "o0vu0Pm6aHehVnc8EmDC7LLn3wqqf9D/78hatdGrZAkt4DbaFk0mmOKTnlDA740D/kxLWwWdskYHOAGoR87Pk9IDk2NwTN8DOPrc"
            + "xKw+i3g7IqxNL46O29nkNfBQS1aN+8g96c2avJ/iKE4P1PirbD4TOjr6xVprHUP+rAlUlG/FB7s4p40AVLa4jHbq90BaOgpo1a7e"
            + "6K264vHW9B8elwE5U13rJBGyG3HeGcCI3kurg9EnDrVv5lEN7FB/IfuxwRmTmea+qNInQgO1pIfsqzuOKSUsk9oXeyJ4BF/3Pdks"
            + "vvr0Gg2ChZFIdTmSpXfMalwrcz7tiwuE27wkGyHFP+lkkjYoEVBHG+2j3UDz1qU3Z+eD1xE6VweGnZSMtOJPToh9UEJ8tz6gdKem"
            + "QsW81Yz1TCPBPhZSYOWSTxU0ylf64YelhRYCjTTMubKViFpQdfNT2IKeW6jHtJ97Zt9saWFR6qVCIEKyZEh0KhG+4c2jMDTjg+WI"
            + "dVRXVh29S5p36WkEslzzYndTIkf+1KWGfFyuhvQNlfeOww0zoPUgJuIAOLwzm97ay6uoBrjAbN8UwVFwCYeMCnvTddwbkqcO3D46"
            + "8AgLKcFldDUbmHMLx4TQ4ZWDGuEbm6ZCeOCE092xDgp0dASt0IFrgKdUkdQYt940TQjmR8KJZLUAXZqPf7mYkwNlnH7PosZoxKo6"
            + "e7XtwkVxv/9z30WMPPaE2vsrEvBsYa0PTPkpevFLmfARDkj5oCm6Q1AOahS9hM9piaLoQlgVoAbAbn+uErfGl3mGEpjejeUF9RNi"
            + "BLZqKzM5imaPhD0x3yL688h+zVDHXnw7fjqJixV58qELPXxgiSy7zBqpPuEFdMy/Il7KXTunH4by0sBVaHy1CLgB3GVTiW87lME6"
            + "Tm8MnseoyfmJ5DvFnl1OuJ9JkMh3/Y7qNOFB7umQknMKe/YGZLiOctu0zLCMOYoVck05MT6wXaUoS2IUb6gP4rooU2rYmfkB9gTu"
            + "7Hfn4kmCr4JDSxmiE0bUWRbvIlpF2oEtcndqdGWhZl2jnNHr0Ny8O/dqMR5AzZi/D+cYYSzMmNsNnVRT6Cwgu6NOxZ9djrO1nEhL"
            + "0hoSgfAIAflsh7WUjwEXJ+3jrD7LD5QToUefxJy20IZDF1BrZsNzOZOhRCbNzQCmVFnT1MrkT3VBH8lEoz6Du4/t5j68h7f4v1UY"
            + "YQVG24gDkzgEdY8UyAOPBm63wlQXl+WjYSeSXWPqzzRMnkHlk1E+le2Crf6obCjIgjoPigrXm1iZMKhkZf3it/zFXCaH4Zr9K30M"
            + "dY2Dhjwclh1ltSKzMHjzvTRXhra0wYs75DE909KndkjEmbSFEWi5iDkzUmoV9AcGXi850ch4t1W4J0rU65lb1uHmGKlzdMwDXRN7"
            + "n7LfozQxHB8xLZ597mkG+0AqCGlOXeSjZa8oZwBMIURVUB23AKYt1Ej71PxUSgmpIf3YwzGSe1uT9psmuesFU+ydnex1S6U0rhFX"
            + "JSBLTaF+Iio+s7zyjdJlaaDAuM8QhvWODdB1voFfVl4jpDHcGNZbbeuxoVAy0yLJQnLqNmwbOho5l1GrxWizaIHWl1KqCP1w7n2A"
            + "P34XMTcNcN7tBU7db/8M5viDXShGMl45VrS+wC1gc8A3tBGpu4t4c5iU+iAmFHpcMYLftqyCga15xCFXvMYZUzNhDftrHGjfssZJ"
            + "3GD04HoJx+e+kz4J+WPS07ioBmvQKNrqsKVCcl8+iwLP7UwSN5sCMus+JYAzMWRzt4V/ABLQiE6/j85/qzFf6hAL+cGJd7hcPKF1"
            + "n6BFbL5Qmu1ua65w0XzUe2hy+BRXtkP7xKqVYuRU5aBqlTRbzPwvoF3HuJCyifZqp//Y4KTNpxT+u2GYG6ojvTpKvJF2jwNv3hWP"
            + "Y0LGSiGHqaeKhABFWEFL/3TSP6Lbi1xvPES+AkKKE1MRnr+XrThKRjIDrSwLTiE2yAXUXCppLv/5rj7M+dwwXs4poHcbCuG8zx/S"
            + "/ZLvccGDgtP4vSL46sHMOgg9IHq1ekq6dSDd6t626mBAHaQUSinznPvSRl0FJKDMfE3ElpxcacdzqBUsNZKpWokcy9IWRbsGkvmn"
            + "PT2Rfp2EFky4+knoFXmM4inZ7WExt/q1/v/asrtKe40ShJm8+73G8gs9GFdspAEb+EuGUd7zSV4LQj/E1H2efxYID1RRcpVtvaD5"
            + "wwSBfbnwqbXDaKoLygnQ64FkBlHXtnAB+CGhHnyiQqA/uzZPpjk1kqChzwbJOJIaTj/OvIa/ud3/uWYHIsn4lBxJjrSjATK3zMeU"
            + "6Omqx05Sur06RWQQGNhpX9DjR9yuC8W1ax2B1Awgyu2LutVUyApmcZbKhnDgqgJh9Lz7jE8sCsdND/M2ptWDDaOBLXaPI6+8bng5"
            + "I4CgHr2v1BXZfXs7Y2T6+pN/vy1NodsbpAKzOFAbn8zcT7C5gfGWlEqMr/qAKI4oJyJ7MaVoeMOdj6dXvuF8lFxKFgymcXW80S9B"
            + "UqALFvV/QYRaDX4Yaa2BZ2Ul2sjNtD3gaipGq1icLePomiumZ1Ux8DoJlmjHB3ltojrpEIECVNQVNvQqIZ8KbA7froOk6YFTByYC"
            + "1/9jsYHxq6FIltw9H7JwlzerxaVY+IVTv/Nin6qxItxKSZ/gpTH+JIRTCd/1z+YC+QGfc240k/uIPrCaiOn8M+rEn3g3o48mWWz/"
            + "hyBiw9LY+jsrqYf7GA+lPsVxOk+NqnLOFTty62lh575yfnW84BifHALv77LAzib8RIMLJjDmIDbl8xfSkB3/a0p3lyqekV5CdCJZ"
            + "mKCbOr9fVIKyvK6ZCIE+iYpURg0H/B/YbvYzNV7Pa0QmfeLjxye8R7l60QIj8uife94eZWHaHcYJTHYyojzQSjkBjG2CBFmACR0d"
            + "qLm4cuz1+6hXR7j6AbX9P6vGRch75JS99PrLmw6GivbwS9jO4u3fFx3Db0iD9cUoUhLryNIRno2Z3OvYn10+pdJe0Fr4V9V73/Xz"
            + "5wSbCFsMiS+clXJjWRGIH5/lBLZGsfAr+CL0mn5rF0YS6rAQ1Kgqtp7T2J4jL1Bw6atuEBE60b8nYqqHeP3kWxLRCCJgL4DIX/zw"
            + "nGWJLCFU92Su+TFbO5PqASfP7W3kfSNzRUNP89ZPi7bd/5YdhSIpdcnPnWwF5kg+oMD3AIHSs6AL1ooU2bcpu4zDfEXrZ+N01kRC"
            + "LuNZSRmv1VTAR1McpZoNLybnx8U5qz9GBAROoltWNGTjlWqzZZ0FyNXbvocqXSqVm1uDp54WeEgN7MD9EQW9pWNzPBqBKRblFooT"
            + "K3fiW+w0a+6TSc/KK/cVVLuAT7507yyqKUnvdsaa9P/5L6ItwNSixjQVzbWuGhhzL6+3VVL8dnVzVywGdJ6JWNHX54JIVwee6ijC"
            + "pKdiDcK8H62WjOCfrpR/Exz/9U/+Z3JmWtY9dSLqiKpPRa9pIrAKMI1qxsp438Cj9pWIFMpe/7UwoXbqNwUjrqaK4WqZCbi7XLEt"
            + "BArdFC7ILR5m0sfKVuHIx9b0YWuWGMQ6Mzeqb2hBnOCtYucu2+DLa/eIK9gsMS7HhoWZIPHRx/JZyU+G5PC/zLKxZ4wWHRmAKnU3"
            + "ReDUuRfIAKMhYjQTgUhjzID0badYrMVWN/BgDwKYpUCKNEQm29XLllMnl2n0piVg84IJnZdb2CENG9srr2d5z//0hHoSnnUbxTtv"
            + "wnHM1UNp2WnDhsoMYr7tQGbc+m9FftZm0FRLPguiUbWDJIYg8OlvWGanhFgdQio1ltNTlr1NtF3t7WfAT7O8B1Ab6EHIrlJJW6vq"
            + "zrbV6OWVgBN7WgZVNpPgOkY1nIT7jy+Z2m73SVNQ3WTWhFHmGPZ1s1vp0vZwlcJqBC8vAsOvOnI8otPkBdjQRw3i5noJ3YPp2Zir"
            + "ym9RDd58amqTLkFoR5sSdMr3kXq0Q9WRrLfZ/RDry4PKi/QBcHrvf6fVnkVg2zdoF3qB/noJNIV1wzKePqP1mYfeTB7keUuF3PWX"
            + "jTPKqacjWMkoO8vugXRaqd6G8p0Scz5Gl4pIzCCvOO+/U9CrTZDPQyeaeP7yiayUQoK92kcy57SYBO5osHyjL2dKHQftW8vTeBXY"
            + "TNac1j6GpARNNhMr6QVky4RiAA8e2G1c3yDE3QkG4xVDdLWDJ5KGWe38098ZapWksADDsmfxzWuJGr4VzFR9ui6ZQynO0tQ+y9QK"
            + "28ZjErmM6CyC2fwpmRsLRHBNH7pWEpvkhabTCd3llCWXqSqryEUwNq1k5VMcxgSvL2IF1BMIINADWbgapl3xTL8hF5du4LRJ/6s8"
            + "jjU7uth3sX6qQoQnX5b5G74ajGsGsnJJxNZjESzI5cMiVvy1xfpwpCF79f6506334Wh6RHscIKfvdp9bpo9EcYK/OuoS0DnmPOfb"
            + "cSeMYqeb7Km4pJgWmWyYPS9dteMCPxJBUWRgcuCNlgsMa6WY4tfjOmMAjwYGz9RaHSv7o6Ez4ADWSwSmsE7dT2TcHhouKxdjgip6"
            + "6LMQtFHHuffUfi/82uzfDohoJb7JR4bhcSWlKkzitKQ9aBAI+iMRaz7jCw9dvaDLBLh+hyoZzg+t5Ok6xnsIlIzSNiBpujsh6PNb"
            + "D4U7NAbuJuuhtDYjxy/6oPOojktR9woAF2tsHBGUiGimm2QlWI7EqErwwUSw4ObO5lXtje1ozNNyMM9YrZKLXgMjdheqz14nirew"
            + "AcX4b849wXrYfOsLWGfr7k2bbcqLsxip61hb90e9xyGjOb/SsL2Ak3B03VT3K4ipxdBvaviGPlMCV5XZ8LVrtyHqVsPAUwj2V0Qy"
            + "LQHdaJ0VrzPSzYuVLviBlYQm+mt+UaKSTslhnIysQj6psjZaR7uC9hcz7c26FuoJW5oW7tfPpzgXN080TGRc54Rr4/TLuN7ghBm/"
            + "o7tomFhRXD3vdf6FTzDf+zGGCz3aVJdIAAQQY8ovJl/Q0X5D7Yi1esNfVlgHqzI0";
}
