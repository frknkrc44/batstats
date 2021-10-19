#!/bin/bash

########################
# Creates a magisk zip #
########################

./gradlew clean

BUILD_TYPE=release
if [ "$BUILD_TYPE" == "release" ]
then
  ./gradlew assembleRelease
else
  ./gradlew assembleDebug
fi

BUILD_DIR=$(pwd)/zip_build
UPDATER_DIR=$BUILD_DIR/META-INF/com/google/android
PERM_DIR=$BUILD_DIR/system/etc/permissions
APP_DIR=$BUILD_DIR/system/priv-app/BatStats

mkdir -p $PERM_DIR
mkdir -p $APP_DIR
cp app/build/outputs/apk/$BUILD_TYPE/app-$BUILD_TYPE.apk $APP_DIR/BatStats.apk
mkdir -p $UPDATER_DIR
curl -L https://raw.githubusercontent.com/topjohnwu/Magisk/master/scripts/module_installer.sh > $UPDATER_DIR/update-binary
if [ $? -ne 0 ]
then
  echo "Download failed, using fallback script"
  cat << EOF > $UPDATER_DIR/update-binary
#!/sbin/sh
umask 022
require_new_magisk() {
  echo "*******************************"
  echo " Please install Magisk v20.4+! "
  echo "*******************************"
  exit 1
}
OUTFD=$2
ZIPFILE=$3
mount /data 2>/dev/null
[ -f /data/adb/magisk/util_functions.sh ] || require_new_magisk
. /data/adb/magisk/util_functions.sh
[ $MAGISK_VER_CODE -lt 20400 ] && require_new_magisk
install_module
exit 0
EOF
fi

echo "#MAGISK" > $UPDATER_DIR/updater-script
cat << EOF > $BUILD_DIR/module.prop
id=batstats12
name=BatStats for Android 12
version=v2.1
versionCode=3
author=frknkrc44
description=Shows battery stats after full charge
EOF

cat << EOF > $PERM_DIR/privapp-permissions-batstats.xml
<permissions>
    <privapp-permissions package="org.fk.batstats">
        <permission name="android.permission.ACCESS_NETWORK_STATE" />
        <permission name="android.permission.BATTERY_STATS" />
        <permission name="android.permission.INTERACT_ACROSS_USERS_FULL" />
    </privapp-permissions>
</permissions>
EOF

pushd $BUILD_DIR
rm -f ../BatStats.zip
zip -r ../BatStats.zip *
popd
rm -rf $BUILD_DIR