confluence-maven-plugin
=======================

## Description

`confluence-maven-plugin` enables you to maintain confluence pages whitin your 
code and update a confluence space when your are ready to do that.   
Pages has to be written using Markdown syntax.

__WARNING This is a *work in progress*__

## Usage

The plugin is deployed on Sonatype Maven repository. So all you need to do is
add these lines in your `pom.xml`:

```xml
    <plugin>
		<groupId>com.github.sixro</groupId>
		<artifactId>confluence-maven-plugin</artifactId>
		<version>0.1.0-SNAPSHOT</version>
		<executions>
			<execution>
				<id>confluence-update</id>
				<goals>
					<goal>update</goal>
				</goals>
				<configuration>
					<server>http://myconfluence:9090/rpc/xmlrpc</server>
					<space>MYSPACE</space>
					<remoteParentPage>MyParent+page</remoteParentPage>
				</configuration>
			</execution>
		</executions>
	</plugin>
```

## Where to get help

To get help, open an issue. In the future I hope to provide help using something
else...

## Contribution guidelines

All contributions are welcome. The project uses a MIT License (as you can see
in the root of the project).
All you need to do is fork the project and send me a pull-request.
Thanks!

## Contributor list

  * [Sixro](http://github.com/sixro)

## Credits, Inspiration, Alternatives

The main reason I created this tool, is the possibility to have on my company
projects the same "feeling" I have with Github projects. I would like to have
a `README.md` in the root of my company projects and I would like to be able to
read the same content on a confluence space.

Another reason is the inspiration I had when I read this post of 
Tom Preston-Werner on [Readme Driven Development](http://tom.preston-werner.com/2010/08/23/readme-driven-development.html).

An alternative to this project is [maven-confluence-plugin](https://code.google.com/p/maven-confluence-plugin/).
It is different from `confluence-maven-plugin`, because it is more involved with maven site style. So
it is useful if what you need is a site style page on your confluence space.
