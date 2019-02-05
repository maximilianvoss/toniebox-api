# TONIEBOX API
You may all know the famous Toniebox [1]. It's a handy storytelling machine for children.
I'm a big fan of this device and therefor I created a small Java library so you can interact with your Tonies easier.

\[1\]: https://tonies.de

# Build
```bash
mvn clean install
```

# Maven Integration
```xml
<dependencies>
    <dependency>
        <groupId>rocks.voss</groupId>
        <artifactId>toniebox-api</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

# Getting Started
```Java
package rocks.voss;

import org.apache.commons.lang3.StringUtils;
import rocks.voss.toniebox.beans.Tonie;
import rocks.voss.toniebox.TonieHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Application {
    public static void main(String[] args) throws IOException {

        TonieHandler tonieHandler = new TonieHandler("USERNAME", "PASSWORD");
        List<Tonie> tonies = tonieHandler.getTonies();

        for (Tonie tonie : tonies) {
            if (StringUtils.equals(tonie.getTonieName(), "TONIES NAME")) {
                
                // delete entire content from the Tonie
                tonieHandler.deleteTonieContent(tonie);
    
                // rename the Tonie
                tonieHandler.changeTonieName(tonie, "Tonies New Name");
				
                // upload file to Tonie
                tonieHandler.uploadFile(tonie, "New Track", "PATH_TO_MP3.mp3");
                return;
            }
        }
    }
}

```

# Known Issues
No issues known yet

# License
Apache License 2.0
