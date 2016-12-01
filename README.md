# JsonApiAdapter
[![](https://jitpack.io/v/vero-amanda/JsonApiAdapter.svg)](https://jitpack.io/#vero-amanda/JsonApiAdapter)

Android [JsonApi][1] Spec v1.0 for [Gson][2]
[1]: http://jsonapi.org
[2]: https://github.com/google/gson

##Download

Root build.grald:

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

Application build.gradle:

    dependencies {
        compile 'com.github.User:Repo:Tag'
    }


##지원되는 기능
| **태그** |  **기능**  |  **읽기**  |  **쓰기**  |  **설명**  |
| :-----: | :-------- | :-------: | :-------: | :-------- |
| 최상위 | Null 기본 데이터 | 예 | 예 | |
| 최상위 | 단일 기본 데이터 | 예 | 예 | |
| 최상위 | 목록 기본 데이터 | 예 | 예 | |
| 최상위 | errors | 예 | 아니오 | |
| 최상위 | links | 예 | 아니오 | |
| 최상위 | jsonapi | 예 | 아니오 | |
| 최상위 | included | 예 | 예 | |
| 기본 데이터 | id tag | 예 | 예 | |
| 기본 데이터 | type tag | 예 | 예 | ```@Type``` Annotation 사용 |
| 기본 데이터 | relationships tag | 예 | 예 | |
| 기본 데이터 | links tag | 예 | 예 | |
| included | id tag | 예 | 예 | |
| included | type tag | 예 | 예 | |
| included | relationships tag | 예 | 아니오 | |
| included | links tag | 예 | 예 | |
| 기본 데이터 <br> relationships | links tag | 예 | 예 | |


##License

    Copyright 2016 Vero
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

