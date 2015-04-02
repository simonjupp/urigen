# URIGen database location #

URIGen is backed by a java based relational database called [HSQLDB](http://hsqldb.org). The database is stored on the URIGen servers filesystem. By default, when URIGen is unpacked by tomcat it will attempt to install this database inside a hidden folder in the users home directory e.g. ~/.urigen

You can change this location by setting the java system property **urigen.home.directory**. (You could also edit the properties file once the urigen.war has unpacked urigen/WEB-INF/classes/urigen.properties)