# アプリナビ Kotlin HandsOn

## 1.2　UIの形や色を変更する

現状のUIは以下のとおりです。<br>

![session1-3-init-View](https://user-images.githubusercontent.com/57338033/156745112-11de1edf-b3ed-475e-8e7e-7778e34d29d5.png)

目標の画面は以下のとおりです。<br>

![session1-3-purpose](https://user-images.githubusercontent.com/57338033/156745130-908ea9fc-4d82-4d01-82f7-abf67e8ef1eb.png)

フォームやボタンの形・全体の色が違いますね<br>
今回は色や形を設定していきます。<br>

## UI制作
まずは`Statusbar`・`ActionBar`の色を変更します。<br>
ちなみに`StatusBar`は`handsOnChatApp`と表示されている紫のバーで、`ActionBar`は充電や時間が記載されている少し濃い紫の部分のことを言います。<br>

![session1-3-explain-bar](https://user-images.githubusercontent.com/57338033/156746591-0c2ada74-ccf8-468b-ada1-696f42e43a34.png)

- `res/values/colors.xml`を開きましょう
- `colors.xml`では色を定義することができます。自分で定義することで使い回しやすくなります。
- 以下の行を`colors.xml`の`<resources></resources>`内に追加しましょう
```
<color name="base_color_blue">#0A84FF</color>
```
- 次に`res/values/themes/themes.xml`を開きましょう。
- `item`の`name`が`colorPrimary`の`@color/purple_500`を`@color/base_color_blue`に変更しましょう。
- 以下に編集後の`theme.xml`を載せておきます

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Theme.HandsOnChatApp" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <!-- Primary brand color. -->
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_700</item>
        <item name="colorOnSecondary">@color/black</item>
        <!-- Status bar color. -->
        <item name="android:statusBarColor" tools:targetApi="l">?attr/colorPrimaryVariant</item>
        <!-- Customize your theme here. -->
    </style>
</resources>
```

- 

<details>
<summary>前回との差分</summary>
<a href="https://github.com/syota-kawaguchi/AppNavi_Kotlin_ChatApp_HandsOn/commit/cdfa306e3c6219f4b983fd9d7addf2d60a545926">diff</a>
</details>

## Next
