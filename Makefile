# Birthday
password = 00000000
encrypt:
	tar --create --file - --gzip -- php | openssl aes-256-cbc -salt -k $(password) -out php.crypt; \
	rm -rf php;

	tar --create --file - --gzip -- DezifyDatabase | openssl aes-256-cbc -salt -k $(password) -out DezifyDatabase.crypt; \
	rm -rf DezifyDatabase;

	tar --create --file - --gzip -- Keystore | openssl aes-256-cbc -salt -k $(password) -out Keystore.crypt; \
	rm -rf Keystore;

decrypt:
	openssl aes-256-cbc -d -salt -k $(password) -in php.crypt | tar -v --extract --gzip --file -; \
	rm -rf php.crypt;

	openssl aes-256-cbc -d -salt -k $(password) -in DezifyDatabase.crypt | tar -v --extract --gzip --file -; \
	rm -rf DezifyDatabase.crypt;

	openssl aes-256-cbc -d -salt -k $(password) -in Keystore.crypt | tar -v --extract --gzip --file -; \
	rm -rf Keystore.crypt;
