SELECT  P.NAME AS NAME ,I.PRODUCT AS ID,I.DMULTIPLY AS QUANTITY,I.RATE,I.TAX1,I.TAX2,I.TAX3,B.CREATEDDATE
FROM BILL B,BILLITEM I,PRODUCTS P ON  WHERE I.PRODUCT=P.ID   GROUP BY P.ID ,I.PRODUCT,I.RATE,I.TAX1ID,I.TAX2ID,I.TAX3ID
