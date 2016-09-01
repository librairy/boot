package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.librairy.model.domain.LinkableElement;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 22/12/15.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, exclude = {"startUri","endUri","weight"},callSuper = true)
public class Relation extends LinkableElement {

    private static final Logger LOG = LoggerFactory.getLogger(Relation.class);

    @Getter @Setter
    public Long id;

    @Getter @Setter
    protected String startUri;

    @Getter @Setter
    protected String endUri;

    @Getter @Setter
    protected Double weight;

    public void between(String startUri,String endUri){
        this.startUri = startUri;
        this.endUri = endUri;
    }

    public void between(LinkableElement start,LinkableElement end){
        this.startUri = start.getUri();
        this.endUri = end.getUri();
    }

    public org.librairy.model.domain.resources.Resource.Type getStartType(){
        return Resource.Type.ANY;
    }

    public org.librairy.model.domain.resources.Resource.Type getEndType(){
        return Resource.Type.ANY;
    }

    public Type getType(){
        return Type.ANY;
    }

    public enum Type{
        AGGREGATES("aggregates","aggregations"),
        PROVIDES("provides","provisions"),
        COMPOSES("composes","compositions"),
        CONTAINS("contains","contains"),
        BUNDLES("bundles","bundles"),
        SIMILAR_TO_DOCUMENTS("similarTo","similarities"),
        SIMILAR_TO_ITEMS("similarTo","similarities"),
        SIMILAR_TO_PARTS("similarTo","similarities"),
        DEALS_WITH_FROM_DOCUMENT("dealsWith","deals"),
        DEALS_WITH_FROM_ITEM("dealsWith","deals"),
        DEALS_WITH_FROM_PART("dealsWith","deals"),
        DESCRIBES("describes","descriptions"),
        EMERGES_IN("emergesIn","emerges"),
        MENTIONS_FROM_TOPIC("mentions","mentions"),
        MENTIONS_FROM_TERM("mentions","mentions"),
        PAIRS_WITH("pairsWith","pairs"),
        EMBEDDED_IN("embeddedIn","embeddings"),
        APPEARED_IN("appearedIn","appearances"),
        HYPERNYM_OF("hypernymOf","hypernyms"),
        ANY("*","*");

        private final String route;
        private final String key;

        public String route(){
            return route;
        }

        public String key() { return key; }

        Type(String key, String route){
            this.key    = key;
            this.route = route;
        }
    }

    public enum State {
        CREATED("created"),
        UPDATED("updated"),
        DELETED("deleted"),
        ANY("*");

        String keyValue;

        State(String key){ keyValue = key;}

        public String key(){ return keyValue;}
    }

    public static Class classOf(Type type){
        switch (type){
            case AGGREGATES: return Aggregates.class;
            case APPEARED_IN: return AppearedIn.class;
            case BUNDLES: return Bundles.class;
            case COMPOSES: return Composes.class;
            case CONTAINS: return Contains.class;
            case DEALS_WITH_FROM_DOCUMENT: return DealsWithFromDocument.class;
            case DEALS_WITH_FROM_ITEM: return DealsWithFromItem.class;
            case DEALS_WITH_FROM_PART: return DealsWithFromPart.class;
            case DESCRIBES: return Describes.class;
            case EMBEDDED_IN: return EmbeddedIn.class;
            case EMERGES_IN: return EmergesIn.class;
            case HYPERNYM_OF: return HypernymOf.class;
            case MENTIONS_FROM_TERM: return MentionsFromTerm.class;
            case MENTIONS_FROM_TOPIC: return MentionsFromTopic.class;
            case PAIRS_WITH: return PairsWith.class;
            case PROVIDES: return Provides.class;
            case SIMILAR_TO_DOCUMENTS: return SimilarToDocuments.class;
            case SIMILAR_TO_ITEMS: return SimilarToItems.class;
            case SIMILAR_TO_PARTS: return SimilarToParts.class;
            default: return Relation.class;
        }
    }

    public static Aggregates newAggregates(String startUri, String endUri){
        return newRelation(Aggregates.class,startUri,endUri);
    }

    public static AppearedIn newAppearedIn(String startUri, String endUri){
        return newRelation(AppearedIn.class,startUri,endUri);
    }

    public static Bundles newBundles(String startUri, String endUri){
        return newRelation(Bundles.class,startUri,endUri);
    }

    public static Composes newComposes(String startUri, String endUri){
        return newRelation(Composes.class,startUri,endUri);
    }

    public static Contains newContains(String startUri, String endUri){
        return newRelation(Contains.class,startUri,endUri);
    }

    public static DealsWithFromDocument newDealsWithFromDocument(String startUri, String endUri){
        return newRelation(DealsWithFromDocument.class,startUri,endUri);
    }

    public static DealsWithFromItem newDealsWithFromItem(String startUri, String endUri){
        return newRelation(DealsWithFromItem.class,startUri,endUri);
    }

    public static DealsWithFromPart newDealsWithFromPart(String startUri, String endUri){
        return newRelation(DealsWithFromPart.class,startUri,endUri);
    }

    public static Describes newDescribes(String startUri, String endUri){
        return newRelation(Describes.class,startUri,endUri);
    }

    public static EmbeddedIn newEmbeddedIn(String startUri, String endUri){
        return newRelation(EmbeddedIn.class,startUri,endUri);
    }

    public static EmergesIn newEmergesIn(String startUri, String endUri){
        return newRelation(EmergesIn.class,startUri,endUri);
    }

    public static HypernymOf newHypernymOf(String startUri, String endUri, String domainUri){
        HypernymOf rel = newRelation(HypernymOf.class, startUri, endUri);
        rel.setDomain(domainUri);
        return rel;
    }

    public static MentionsFromTerm newMentionsFromTerm(String startUri, String endUri){
        return newRelation(MentionsFromTerm.class,startUri,endUri);
    }

    public static MentionsFromTopic newMentionsFromTopic(String startUri, String endUri){
        return newRelation(MentionsFromTopic.class,startUri,endUri);
    }

    public static PairsWith newPairsWith(String startUri, String endUri, String domainUri){
        PairsWith rel = newRelation(PairsWith.class, startUri, endUri);
        rel.setDomain(domainUri);
        return rel;
    }

    public static Provides newProvides(String startUri, String endUri){
        return newRelation(Provides.class,startUri,endUri);
    }

    public static SimilarToDocuments newSimilarToDocuments(String startUri, String endUri, String domainUri){
        SimilarToDocuments rel = newRelation(SimilarToDocuments.class, startUri, endUri);
        rel.setDomain(domainUri);
        return rel;
    }

    public static SimilarToItems newSimilarToItems(String startUri, String endUri, String domainUri){
        SimilarToItems rel = newRelation(SimilarToItems.class, startUri, endUri);
        rel.setDomain(domainUri);
        return rel;
    }

    public static SimilarToParts newSimilarToParts(String startUri, String endUri, String domainUri){
        SimilarToParts rel = newRelation(SimilarToParts.class, startUri, endUri);
        rel.setDomain(domainUri);
        return rel;
    }

    private static <T extends Relation> T newRelation(Class<T> clazz, String su, String eu){
        T instance = null;
        try {
            instance = clazz.newInstance();
            instance.setStartUri(su);
            instance.setEndUri(eu);
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Error creating instance of a relation: " + clazz);
        }
        return instance;
    }

    public Aggregates asAggregates(){
        return Aggregates.class.cast(this);
    }

    public AppearedIn asAppearedIn(){
        return AppearedIn.class.cast(this);
    }

    public Bundles asBundles(){
        return Bundles.class.cast(this);
    }

    public Composes asComposes(){
        return Composes.class.cast(this);
    }

    public Contains asContains(){
        return Contains.class.cast(this);
    }

    public DealsWithFromDocument asDealsWithFromDocument(){
        return DealsWithFromDocument.class.cast(this);
    }

    public DealsWithFromItem asDealsWithFromItem(){
        return DealsWithFromItem.class.cast(this);
    }

    public DealsWithFromPart asDealsWithFromPart(){
        return DealsWithFromPart.class.cast(this);
    }

    public Describes asDescribes(){
        return Describes.class.cast(this);
    }

    public EmbeddedIn asEmbeddedIn(){
        return EmbeddedIn.class.cast(this);
    }

    public EmergesIn asEmergesIn(){
        return EmergesIn.class.cast(this);
    }

    public HypernymOf asHypernymOf(){
        return HypernymOf.class.cast(this);
    }

    public MentionsFromTerm asMentionsFromTerm(){
        return MentionsFromTerm.class.cast(this);
    }

    public MentionsFromTopic asMentionsFromTopic(){
        return MentionsFromTopic.class.cast(this);
    }

    public PairsWith asPairsWith(){
        return PairsWith.class.cast(this);
    }

    public Provides asProvides(){
        return Provides.class.cast(this);
    }

    public SimilarToDocuments asSimilarToDocuments(){
        return SimilarToDocuments.class.cast(this);
    }

    public SimilarToItems asSimilarToItems(){
        return SimilarToItems.class.cast(this);
    }

    public SimilarToParts asSimilarToParts(){
        return SimilarToParts.class.cast(this);
    }

}
