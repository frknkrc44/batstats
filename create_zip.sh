#!/bin/bash

########################
# Creates a magisk zip #
########################

./gradlew assembleDebug

BUILD_DIR=$(pwd)/zip_build
UPDATER_DIR=$BUILD_DIR/META-INF/com/google/android
PERM_DIR=$BUILD_DIR/system/etc/permissions
APP_DIR=$BUILD_DIR/system/priv-app/BatStats

mkdir -p $PERM_DIR
mkdir -p $APP_DIR
cp app/build/outputs/apk/debug/app-debug.apk $APP_DIR
mkdir -p $UPDATER_DIR
curl -L https://raw.githubusercontent.com/topjohnwu/Magisk/master/scripts/module_installer.sh > $UPDATER_DIR/update-binary
echo "#MAGISK" > $UPDATER_DIR/updater-script
cat << EOF > $BUILD_DIR/module.prop
id=batstats12
name=BatStats for Android 12
version=v1.0
versionCode=1
author=frknkrc44
description=Shows screen and last charge time
EOF

cat << EOF > $PERM_DIR/privapp-permissions-batstats.xml
<permissions>
    <privapp-permissions package="org.fk.batstats">
        <permission name="android.permission.BATTERY_STATS" />
    </privapp-permissions>
</permissions>
EOF

pushd $BUILD_DIR
rm -f ../BatStats.zip
zip -r ../BatStats.zip *
popd $BUILD_DIR
