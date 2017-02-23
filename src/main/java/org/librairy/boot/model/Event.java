/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model;

import lombok.Data;

import java.io.*;

@Data
public class Event {

    private byte[] bytes;

	private Event(byte[] bytes){
        this.bytes = bytes;
    }

    public String toString(){
        try {
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    public static <T> Event from( T element ){
        try {
            if (element instanceof String){
                return new Event(((String) element).getBytes());
            } else if (element instanceof byte[]){
                return new Event((byte[])element);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(element);
            out.flush();
            return new Event(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T to(Class<T> classType){
        try {
            if (classType.equals(String.class)){
               return (T) toString();
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in = new ObjectInputStream(bis);
            Object data = in.readObject();
            in.close();
            return (T) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
