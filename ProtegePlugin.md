# Introduction #

You can use the URIGen Protege 4.1 plugin to access a URIGen server from Protege. You can map an ontology URI to a URIGen server, so that each time a new entity is created in Protege, it will get the next URI in sequence for that ontology.

## Installation ##

Download the org.protege.urigen.jar from the downloads page. Put this jar inside your Protege home directory, inside the plugins folder. Restart protege.

## URIGen Protege setup ##

To activate URIGen within Protege, open the Protege preferences and navigate to the "New entities server" Tab.

![http://urigen.googlecode.com/svn/trunk/screenshots/10.png](http://urigen.googlecode.com/svn/trunk/screenshots/10.png)

  * Check the use URIgen server checkbox.
  * Select the add a new URIGen server connection button
    * Enter the URL to your URIGen server e.g. http://localhost:8080/urigen
    * Press the connect button, the dropdown box below should fill with a list of available preferences.
    * Select a preference from the list
    * Finally enter your API key to access this server. If you don't know your API key, either contact the server admin, or log into the server via the web interface and click on the link to "show me my API key".
    * Select Apply to activate the URIGen server

![http://urigen.googlecode.com/svn/trunk/screenshots/11.png](http://urigen.googlecode.com/svn/trunk/screenshots/11.png)


## Creating new entities ##
If the URIGen server is live, you can use Protege as normal. If you are working on an ontology with a URI that matches one of the URIGen preferences, all new URI creations will be executed via the URIGen server.

If you are working on a n ontology that is linked to aURIGen server, you will notice that any new entity is assigned a temporary internal URI. This is randomly generated, once you commit to creating the new entity, Protege will ping the URIGen server to get the next available URI in sequence and use it to create your new entity in your ontology.

![http://urigen.googlecode.com/svn/trunk/screenshots/15.png](http://urigen.googlecode.com/svn/trunk/screenshots/15.png)

If for any reason something goes wrong, or the URIGen server is down, Protege will simply assign the random URI to the new entity. You can easily update these at a later date when the URIGen server becomes available again.

![http://urigen.googlecode.com/svn/trunk/screenshots/16.png](http://urigen.googlecode.com/svn/trunk/screenshots/16.png)

You can see all new entities created by all users back on the URIGen web interface.


![http://urigen.googlecode.com/svn/trunk/screenshots/14.png](http://urigen.googlecode.com/svn/trunk/screenshots/14.png)

## Protege new entities preferences ##

If your active ontology is not managed by one of your URIGen repositories, or your network connection to the URIGen server is down, Protege will revert to your default 'New entities' policy.

If you are working on an active ontology that is managed by URIGen, but an error occurred communicating with the server, or the server was down, Protege will assign a random URI to your new entity currently of the from: http://urigen_local_uri/ + random string. You can always generate new URIs when you get back online, and refactor any temporary URIs to the new generated URIs.

## Label rendering ##

If you are using some auto id system for new entities, you often want to assign a label on creation. You can set Protege and URIGen up so that when you create a new entity, you only have to enter the label and the URI creation happens for you. For this to work, you need to have your preferences configured correctly. In the Protege preferences, under the 'New entities' tab, inside the 'Entity label' box, make sure "Create label from user-supplied name" is checked.