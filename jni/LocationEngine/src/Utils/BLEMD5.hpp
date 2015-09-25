//
//  BLEMD5.hpp
//  BLEProject
//
//  Created by innerpeacer on 15/9/24.
//  Copyright © 2015年 innerpeacer. All rights reserved.
//

#ifndef BLEMD5_hpp
#define BLEMD5_hpp

#include <string>
#include <fstream>

typedef unsigned char byte;
typedef unsigned int uint32;

using std::string;
using std::ifstream;

namespace Innerpeacer {
    namespace BLELocationEngine {
        
        class BLEMD5 {
        public:
            BLEMD5();
            BLEMD5(const void *input, size_t length);
            BLEMD5(const string &str);
            BLEMD5(ifstream &in);
            void update(const void *input, size_t length);
            void update(const string &str);
            void update(ifstream &in);
            const byte* digest();
            string toString();
            void reset();
        private:
            void update(const byte *input, size_t length);
            void final();
            void transform(const byte block[64]);
            void encode(const uint32 *input, byte *output, size_t length);
            void decode(const byte *input, uint32 *output, size_t length);
            string bytesToHexString(const byte *input, size_t length);
            
            /* class uncopyable */
            BLEMD5(const BLEMD5&);
            BLEMD5& operator=(const BLEMD5&);
        private:
            uint32 _state[4];	/* state (ABCD) */
            uint32 _count[2];	/* number of bits, modulo 2^64 (low-order word first) */
            byte _buffer[64];	/* input buffer */
            byte _digest[16];	/* message digest */
            bool _finished;		/* calculate finished ? */
            
            static const byte PADDING[64];	/* padding for calculate */
            static const char HEX[16];
            static const size_t BUFFER_SIZE = 1024;
        };
    }
}


#endif /* BLEMD5_hpp */
