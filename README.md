# Radius_Search
MySQL, PostgreSQL+PostGis 반경 검색 기능 측정

## 1만건 기준
PostgreSQL ST_Dwithin : 약 20ms

MySQL ST_Contains+ST_Distance_Sphere 함께 사용 : 약 315ms

PostgreSQL ST_DistanceSphere : 약 400ms

MySQL ST_Distance_Sphere 함께 사용 : 약 475ms

