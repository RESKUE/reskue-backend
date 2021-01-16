# Start

## Nominatim
/osm/nominatim-docker/3.5:
```
sudo docker run --restart=always -p 6432:5432 -p 7070:8080 --name nominatim -v /media/tim/SSD_Ubuntu1/osm/data/germany/postgresdata:/var/lib/postgresql/12/main nominatim bash /app/start.sh
```

## OSM-Website
/openstreetmap-website:
```
vagrant up --provider virtualbox
vagrant ssh -- -R 7070:localhost:7070
```
-> on vagrant server:
```
cd /srv/openstreetmap-website/
rails server --binding=0.0.0.0
```

#### Verify user instead of email
- https://github.com/openstreetmap/openstreetmap-website/blob/master/CONFIGURE.md#managing-users

#### Setup iD editor
- https://github.com/openstreetmap/openstreetmap-website/blob/master/CONFIGURE.md#oauth-consumer-keys
- into /srv/openstreetmap-website/public/land.html: https://github.com/openstreetmap/iD/blob/develop/dist/land.html

## Overpass

#### Karlsruhe
```
docker run \
-e OVERPASS_META=yes \
-e OVERPASS_MODE=init \
-e OVERPASS_PLANET_URL=http://download.geofabrik.de/europe/germany/baden-wuerttemberg/karlsruhe-regbez-latest.osm.pbf \
-e OVERPASS_DIFF_URL=https://osm-internal.download.geofabrik.de/europe/germany/baden-wuerttemberg/karlsruhe-regbez-updates \
-e OVERPASS_RULES_LOAD=10 \
-e OVERPASS_COMPRESSION=gz \
-e OVERPASS_UPDATE_SLEEP=3600 \
-e OVERPASS_PLANET_PREPROCESS='mv /db/planet.osm.bz2 /db/planet.osm.pbf && osmium cat -o /db/planet.osm.bz2 /db/planet.osm.pbf && rm /db/planet.osm.pbf' \
-e USE_OAUTH_COOKIE_CLIENT=yes \
--mount type=bind,source=/media/tim/SSD_Ubuntu1/overpass/data/oauth-settings.json,target=/secrets/oauth-settings.json \
-v /media/tim/SSD_Ubuntu1/overpass/data/db/:/db \
-p 12347:80 \
-i -t \
--name overpass_karlsruhe wiktorn/overpass-api
```

##### oauth-settings.json:
```json
{
  "user": "OSM_USERNAME",
  "password": "OSM_PASSWORD",
  "osm_host": "https://www.openstreetmap.org",
  "consumer_url": "https://osm-internal.download.geofabrik.de/get_cookie"
}
```
# Create cultural asset and sync with Overpass API
TL;DR:
1. OSM API: Create changeset -> changeset id
2. OSM API: Create node -> node id
3. OSM API: Download changeset -> xml file
4. move xml file to overpass/db/diffs/XML_FILE
5. change extension of xml file to .osm
6. execute ./app/bin/update_overpass.sh in Overpass docker container
7. DONE
#### OSM API: Create changeset
Set BASIC_AUTH_TOKEN, CULTURAL_ASSET_ID
```
curl --location --request PUT 'http://localhost:3000/api/0.6/changeset/create' \
--header 'Authorization: Basic BASIC_AUTH_TOKEN' \
--header 'Content-Type: application/xml' \
--data-raw '<?xml version='\''1.0'\'' encoding='\''UTF-8'\''?>
<osm version='\''0.6'\'' generator='\''KUERES'\''>
    <changeset>
        <tag k="comment" v="create cultural asset with id CULTURAL_ASSET_ID"/>
    </changeset>
</osm>'
```
> 200: returns CHANGESET_ID as text/plain
#### OSM API: Create node
Set BASIC_AUTH_TOKEN, CHANGESET_ID, LATITUDE, LONGITUDE, CULTURAL_ASSET_ID
```
curl --location --request PUT 'http://localhost:3000/api/0.6/node/create' \
--header 'Authorization: Basic BASIC_AUTH_TOKEN' \
--header 'Content-Type: application/xml' \
--data-raw '<?xml version='\''1.0'\'' encoding='\''UTF-8'\''?>
<osm version='\''0.6'\'' generator='\''JOSM'\''>
    <node changeset="CHANGESET_ID" lat="LATITUDE" lon="LONGITUDE">
        <tag k="culturalAsset" v="CULTURAL_ASSET_ID"/>
    </node>
</osm>'
```
> 200: returns NODE_ID as text/plain
#### OSM API: Download changeset
Set CHANGESET_ID
```
curl --location --request GET 'http://localhost:3000/api/0.6/changeset/CHANGESET_ID/download'
```
#### Move XML file containing the changeset
Set DOWNLOAD_LOCATION and OVERPASS_LOCATION
```
mv DOWNLOAD_LOCATION/changes.xml OVERPASS_LOCATION/db/diffs/changes.osm
```
#### Execute Overpass update
```
docker exec -it overpass_karlsruhe ./app/bin/update_overpass.sh
```

# NOTES:
- osm-settings: /srv/openstreetmap-website/config/settings.yml
	- URL for nominatim
  - URL for overpass
- nominatim doesnt start: user id error
	- attach shell
	- service postgresql start


# Links:
- https://github.com/mediagis/nominatim-docker/tree/master/3.5
- https://github.com/openstreetmap/openstreetmap-website/blob/master/VAGRANT.md
- https://wiki.openstreetmap.org/wiki/API_v0.6
- https://gis.stackexchange.com/questions/336151/data-is-not-appearing-in-a-local-osm-server-after-applying-changes-to-it/337405#337405
