module rocks.voss.toniebox.api {
    requires lombok;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpmime;
    requires org.apache.commons.codec;
    requires org.apache.commons.io;
    requires commons.lang3;
    requires log4j;
    requires jackson.core;
    requires jackson.annotations;
    requires jackson.databind;

    exports rocks.voss.toniebox;
    exports rocks.voss.toniebox.beans.toniebox;
}
