The library use com.google.code.gson to encode and decode json to and from Globs.
It produces standard json and mostly all the json can be read directly to globs.
There is additinal annotation to parse json where a field is a value. For exemple the OpenApi json format put the http code as value:
```
...
   200: {
       ...
       },
   204: {
       }    
```

When the Gson is read and write using Globs the attribut _kind can be added. It allow the reader to instantiate the wright Glob by finding the GlobType in the Model.

For exemple a Shopify product like :
```
{
  "admin_graphql_api_id": "gid:\/\/shopify\/Product\/6918907461686",
  "body_html": "description du produit",
  "created_at": "2023-02-14T02:30:41+01:00",
  "handle": "lien-vers-le-produit",
  "id": 6918907461686,
  "product_type": "Mercerie",
  "published_at": "2023-02-14T02:30:41+01:00",
  "template_suffix": null,
  "title": "le titre du produit",
  "updated_at": "2024-04-14T15:56:13+02:00",
  "vendor": "Osborne",
  "status": "active",
  "published_scope": "web",
  "tags": "",
  "variants": [
    {
      "admin_graphql_api_id": "gid:\/\/shopify\/ProductVariant\/40658354864182",
      "barcode": "0096685150284",
      "compare_at_price": null,
      "created_at": "2023-02-14T02:30:41+01:00",
      "fulfillment_service": "manual",
      "id": 40658354864182,
      "inventory_management": "shopify",
      "inventory_policy": "deny",
      "position": 1,
      "price": "1.66",
```

Can be read using GlobType : 
```
public class ShopifyProductType {
    public static final String resourceType = "product";

    @ShopifyResourceType_(resourceType)
    public static GlobType TYPE;

    @KeyField
    @GraphqlName_("legacyResourceId")
    public static LongField id;

    @GraphqlName_("id")
    @FieldNameAnnotation("admin_graphql_api_id")
    public static StringField admin_graphql_api_id;

    @FieldNameAnnotation("created_at")
    public static DateTimeField created_at;

    @FieldNameAnnotation("updated_at")
    public static DateTimeField updated_at;

    public static StringField title;

    public static StringField handle;

    public static StringField vendor;

    @FieldNameAnnotation("body_html")
    @GraphqlName_("descriptionHtml")
    public static StringField body_html;

    @FieldNameAnnotation("options")
    @Target(ShopifyProductOptionType.class)
    public static GlobArrayField options;

    @Target(ShopifyVariantType.class)
    @ShopifyConnection_
    public static GlobArrayField variants;

```