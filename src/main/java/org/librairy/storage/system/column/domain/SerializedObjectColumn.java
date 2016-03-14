package org.librairy.storage.system.column.domain;

import com.datastax.driver.core.utils.Bytes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "serializations")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "instance",callSuper = true)
public class SerializedObjectColumn extends org.librairy.model.domain.resources.SerializedObject {

    private static final Logger LOG = LoggerFactory.getLogger(SerializedObjectColumn.class);

    @PrimaryKey
    private String uri;

    @Override
    public void setInstance(Object instance) {
        String hexString="";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bytes);
            oos.writeObject(instance);
            hexString = Bytes.toHexString(bytes.toByteArray());
        } catch (IOException e) {
            LOG.error("Error setting instance of serialized object: " + instance, e);
        }
        this.instance = Bytes.fromHexString(hexString);
    }


    @Override
    public Object getInstance() {
        if (instance != null){
            try {
                ByteBuffer byteBuffer = (ByteBuffer) instance;
                String hx = Bytes.toHexString(byteBuffer);
                ByteBuffer ex = Bytes.fromHexString(hx);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(ex.array()));
                Object data = ois.readObject();
                return data;
            } catch (IOException | ClassNotFoundException e) {
                LOG.error("Error getting instance of serialized object: " + instance, e);
            }
        }
        return null;
    }

}
