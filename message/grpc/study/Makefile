.PHONY: proto_before proto_after proto_format clean

BEFORE_PATH=productinfo

proto_before: proto_format
	protoc \
	-I=./${BEFORE_PATH} \
	--go_out=:./${BEFORE_PATH} \
	--go_opt=module=github.com/cid-yoon/grpc/${BEFORE_PATH} \
	./${BEFORE_PATH}/proto/*.proto

AFTER_PATH=after

proto_after: proto_format
	protoc \
	-I=./${AFTER_PATH} \
	--go_out=:./${AFTER_PATH} \
	--go_opt=module=github.com/cid-yoon/grpc-sample/go-package-option/${AFTER_PATH} \
	./${AFTER_PATH}/team/*.proto \
	./${AFTER_PATH}/person/*.proto

proto_format:
	find ./before -name "*.proto"
	find ./after -name "*.proto"

clean:
	find ./before -name "*.pb.go" | xargs rm
	find ./after -name "*.pb.go" | xargs rm


