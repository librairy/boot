/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.google.common.base.Strings;

import java.util.Optional;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class AnnotationFilter {

    public Optional<String> type    = Optional.empty();
    public Optional<String> purpose = Optional.empty();
    public Optional<String> creator = Optional.empty();

    private AnnotationFilter(){

    }

    public static AnnotationFilterByType byType(String type){
        return new AnnotationFilterByType(type);
    }

    public static class AnnotationFilterByType{

        public final String type;

        private AnnotationFilterByType(String type){
            this.type = type;
        }

        public AnnotationFilterByPurpose byPurpose(String purpose){
            return new AnnotationFilterByPurpose(this, purpose);
        }

        public AnnotationFilter build(){
            AnnotationFilter filter = new AnnotationFilter();
            if (!Strings.isNullOrEmpty(type)) filter.type     = Optional.of(this.type);
            return filter;
        }
    }

    public static class AnnotationFilterByPurpose{

        public final String purpose;
        public AnnotationFilterByType typeFilter;

        private AnnotationFilterByPurpose(AnnotationFilterByType type, String purpose){
            this.purpose = purpose;
            this.typeFilter = type;
        }

        public AnnotationFilterByCreator byCreator(String creator){
            return new AnnotationFilterByCreator(this , creator);
        }

        public  AnnotationFilter build(){
            AnnotationFilter filter = new AnnotationFilter();
            if (!Strings.isNullOrEmpty(purpose)) filter.purpose  =  Optional.of(this.purpose);
            if (!Strings.isNullOrEmpty(this.typeFilter.type)) filter.type  = Optional.of(this.typeFilter.type);
            return filter;
        }
    }

    public static class AnnotationFilterByCreator{
        public final String creator;
        public AnnotationFilterByPurpose purposeFilter;

        private AnnotationFilterByCreator(AnnotationFilterByPurpose purpose, String creator){
            this.creator = creator;
            this.purposeFilter = purpose;
        }

        public  AnnotationFilter build(){
            AnnotationFilter filter = new AnnotationFilter();
            if (!Strings.isNullOrEmpty(creator)) filter.creator  = Optional.of(this.creator);
            if (!Strings.isNullOrEmpty(this.purposeFilter.purpose)) filter.purpose  = Optional.of(this.purposeFilter.purpose);
            if (!Strings.isNullOrEmpty(this.purposeFilter.typeFilter.type)) filter.type     = Optional.of(this.purposeFilter.typeFilter.type);
            return filter;
        }
    }
}
