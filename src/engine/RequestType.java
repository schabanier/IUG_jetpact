package engine;

/**
 * Created by propriétaire on 14/03/2015.
 */
public enum RequestType {

    MODIFY_EMAIL,
    MODIFY_PASSWORD,
    MODIFY_BRACELET_UID,

    ADD_TAG,
    MODIFY_TAG_OBJECT_NAME,
    MODIFY_TAG_OBJECT_IMAGE,
    REMOVE_TAG,

    CREATE_PROFILE_WITH_TAGS,
    ADD_TAGS_TO_PROFILE,
    REMOVE_TAGS_FROM_PROFILE,
    REPLACE_TAG_LIST_OF_PROFILE,
    MODIFY_PROFILE_NAME,
    REMOVE_PROFILE

}
