/**
 * PostgREST API
 * standard public schema
 *
 * OpenAPI spec version: 0.5.0.0 (903a8d5)
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


export interface ServiceRegistry {
    /**
     * Note: This is a Primary Key.<pk/>
     */
    serviceId?: string;
    codeProviderId?: string;
    serviceSpecification?: string;
    servicePolicy?: string;
    hardware?: string;
}
