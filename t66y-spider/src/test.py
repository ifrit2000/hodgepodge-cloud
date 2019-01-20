# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'dialog.ui'
#
# Created by: PyQt5 UI code generator 5.9.2
#
# WARNING! All changes made in this file will be lost!

from PyQt5 import QtCore, QtWidgets


class Ui_Dialog(object):
    def setupUi(self, Dialog):
        Dialog.setObjectName("Dialog")
        Dialog.resize(1175, 640)
        self.pushButton = QtWidgets.QPushButton(Dialog)
        self.pushButton.setGeometry(QtCore.QRect(260, 420, 80, 25))
        self.pushButton.setObjectName("pushButton")

        self.retranslateUi(Dialog)
        # self.pushButton.clicked.connect(Dialog.close)
        self.pushButton.clicked.connect(slot=self.test)
        QtCore.QMetaObject.connectSlotsByName(Dialog)

    def retranslateUi(self, Dialog):
        _translate = QtCore.QCoreApplication.translate
        Dialog.setWindowTitle(_translate("Dialog", "Dialog"))
        self.pushButton.setText(_translate("Dialog", "PushButton"))

    def test(self):
        print("123123")


if __name__ == '__main__':
    import sys

    app = QtWidgets.QApplication(sys.argv)
    formObj = QtWidgets.QDialog()  # 注意，这里和我们一开始创建窗体时使用的界面类型相同
    ui = Ui_Dialog()
    ui.setupUi(formObj)
    formObj.show()
    sys.exit(app.exec_())
